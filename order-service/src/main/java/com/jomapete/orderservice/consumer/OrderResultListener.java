package com.jomapete.orderservice.consumer;

import com.jomapete.orderservice.dto.OrbitResult;
import com.jomapete.orderservice.entity.OrderState;
import com.jomapete.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class OrderResultListener {
    @Autowired
    private OrderRepository orderRepository;

    @RabbitListener(queues = "orbit-results-queue")
    public void handleResult(OrbitResult result) {
        String id = result.getOriginalOrderId();
        // Look for the id on our db
        Optional<OrderState> orderOpt = orderRepository.findById(id);
        // Recover current state from DB
        if (orderOpt.isEmpty()) {
            System.out.println("Order not found on DB: " + id);
            return;
        }
        OrderState order = orderOpt.get();


        if ("COMPLETED".equals(result.getStatus())) {
            order.setStatus("COMPLETED");
            orderRepository.save(order);
            System.out.println("Successfully completed order: " + order.getId());
        }
        // --- SAGA PATTERN: UPDATE STATE ---
        else if ("FAILED".equals(result.getStatus())) {
            // --- COMPENSATING TRANSACTION  ---
            order.setStatus("CANCELLED");
            orderRepository.save(order); // UPDATE
            // COMPENSATING TRANSACTION(ROLLBACK)
            System.out.println("Your order has been cancelled: " + order.getId());

        }
    }
}
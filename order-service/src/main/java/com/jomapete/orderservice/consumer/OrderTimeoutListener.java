package com.jomapete.orderservice.consumer;

import com.jomapete.orderservice.dto.OrderEvent;
import com.jomapete.orderservice.entity.OrderState;
import com.jomapete.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderTimeoutListener {

    private final OrderRepository orderRepository;

    // Usamos inyección por constructor, ¡como aprendimos al principio!
    public OrderTimeoutListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RabbitListener(queues = "order-timeout-queue")
    public void handleOrderTimeout(OrderEvent event) {
        System.out.println("SAGA TIMEOUT: No response from Inventory for Order ID: " + event.getId());

        Optional<OrderState> orderOpt = orderRepository.findById(event.getId());

        if (orderOpt.isPresent()) {
            OrderState order = orderOpt.get();

            // cancel if status is PENDING
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("CANCELLED_BY_TIMEOUT");
                orderRepository.save(order);
                System.out.println("Order " + event.getId() + " has been auto-cancelled due to timeout.");
            }
        }
    }
}
package com.jomapete.orderservice.service.impl;

import com.jomapete.orderservice.dto.OrbitResult;
import com.jomapete.orderservice.dto.OrderEvent;
import com.jomapete.orderservice.entity.OrderState;
import com.jomapete.orderservice.repository.OrderRepository;
import com.jomapete.orderservice.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void processOrbitResult(OrbitResult result) {
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
        // SAGA PATTERN: UPDATE STATE
        else if ("FAILED".equals(result.getStatus())) {
            //  COMPENSATING TRANSACTION
            order.setStatus("CANCELLED");
            orderRepository.save(order); // UPDATE
            // COMPENSATING TRANSACTION(ROLLBACK)
            System.out.println("Your order has been cancelled: " + order.getId());

        }
    }
    @Override
    public  OrderEvent createOrder(OrderEvent request) {
        // 1. DEFAULT VALUES
        request.setId(UUID.randomUUID().toString());
        if (request.getAction() == null || request.getAction().isEmpty()) {
            request.setAction("CALCULATE_SAFE_NAVIGATION");
        }
        // 2. Save Order State
        OrderState order = new OrderState(request.getId(), "PENDING");
        orderRepository.save(order);
        System.out.println("Order ID saved on DB-ORDER: " + order.getId());

        // 3. PRODUCER PATTERN (Asynchronous Communication) 📨
        // We publish the event to the "orbit-tasks-queue" (TO THE EXCHANGE)
        // Using RabbitMQ decouples the Order Service from the Worker.
        rabbitTemplate.convertAndSend("orbit-tasks-queue", request);

        return request;

    }

}

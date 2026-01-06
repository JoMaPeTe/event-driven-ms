package com.jomapete.orderservice.controller;



import com.jomapete.orderservice.entity.OrderState;
import com.jomapete.orderservice.dto.OrderEvent;
import com.jomapete.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public String launchSatelliteTask(@RequestBody OrderEvent request) {
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

        return "Task submitted regarding satellite: %s (ID: %s)"
                .formatted(request.getSatelliteName(), request.getId());
    }
}

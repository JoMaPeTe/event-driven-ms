package com.jomapete.orderservice.consumer;

import com.jomapete.orderservice.dto.OrbitResult;
import com.jomapete.orderservice.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderResultListener {
    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "orbit-results-queue")
    public void handleResult(OrbitResult result) {
        orderService.processOrbitResult(result);
    }
}
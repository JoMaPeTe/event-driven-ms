package com.jomapete.orderservice.controller;

import com.jomapete.orderservice.dto.OrderEvent;
import com.jomapete.orderservice.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping
    public String launchSatelliteTask(@RequestBody OrderEvent request) {
        OrderEvent processedEvent = orderService.createOrder(request);

        return "Task submitted regarding satellite: %s (ID: %s)"
                .formatted(processedEvent.getSatelliteName(), processedEvent.getId());
    }

}

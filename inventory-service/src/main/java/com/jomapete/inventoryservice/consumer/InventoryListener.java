package com.jomapete.inventoryservice.consumer;

import com.jomapete.inventoryservice.dto.OrderEvent;
import com.jomapete.inventoryservice.service.InventoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {
    @Autowired
    private InventoryService inventoryService;

    @RabbitListener(queues = "orbit-tasks-queue")
    public void processOrbit(OrderEvent event) {
        inventoryService.handleOrbitProcess(event);
    }
}
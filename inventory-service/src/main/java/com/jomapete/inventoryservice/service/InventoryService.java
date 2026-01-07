package com.jomapete.inventoryservice.service;

import com.jomapete.inventoryservice.dto.OrderEvent;

public interface InventoryService {
    void handleOrbitProcess(OrderEvent event);
    void undoOperation(String id);
}
package com.jomapete.inventoryservice;

import com.jomapete.inventoryservice.consumer.InventoryListener;
import com.jomapete.inventoryservice.dto.OrderEvent;
import com.jomapete.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // 1. Arranca Mockito (sin levantar todo Spring)
class InventoryListenerTest {

    @Mock // Simulate Service
    private InventoryService inventoryService;


    @InjectMocks // Inyectamos simulations in our real class
    private InventoryListener inventoryListener;

    @Test
    void listenerShouldDelegateToService() {
        OrderEvent event = new OrderEvent("1", "SAT", "CALC");
        inventoryListener.processOrbit(event);
        verify(inventoryService, times(1)).handleOrbitProcess(event);
    }
}
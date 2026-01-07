package com.jomapete.inventoryservice;

import com.jomapete.inventoryservice.dto.OrbitResult;
import com.jomapete.inventoryservice.dto.OrderEvent;
import com.jomapete.inventoryservice.repository.ProcessedEventRepository;
import com.jomapete.inventoryservice.service.impl.InventoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock // Simulate RabbitMQ
    private RabbitTemplate rabbitTemplate;


    @Mock // Simulate Mongo DataBase
    private ProcessedEventRepository repository;

    @InjectMocks // Inyectamos simulations in our real class
    private InventoryServiceImpl inventoryService;

    @Test
    void shouldProcessSuccess_WhenSatelliteIsNormal() {
        // GIVEN (Normal Event)
        OrderEvent event = new OrderEvent("uuid-123", "Sentinel-6", "CALCULATE");
        // Simulate that the ID doesn´t exist on DB (it´s not duplicate)
        when(repository.existsById("uuid-123")).thenReturn(false);

        // WHEN (processed)
        inventoryService.handleOrbitProcess(event);

        // THEN
        // 1. Verify the data saved on Mongo
        verify(repository, times(1)).save(any());

        // 2. CAPTURE the message that was sent back to RabbitMQ
        ArgumentCaptor<OrbitResult> captor = ArgumentCaptor.forClass(OrbitResult.class);
        verify(rabbitTemplate).convertAndSend(eq("orbit-results-queue"), captor.capture());

        // 3. Verify status is COMPLETED
        OrbitResult result = captor.getValue();
        assertEquals("COMPLETED", result.getStatus());
        assertEquals("Calculation successful.", result.getDetails());
    }

    @Test
    void shouldReturnFailed_WhenSatelliteIsError() {
        // GIVEN (a satellite called "ERROR")
        OrderEvent event = new OrderEvent("uuid-999", "ERROR", "CALCULATE");
        when(repository.existsById("uuid-999")).thenReturn(false);

        // WHEN
        inventoryService.handleOrbitProcess(event);

        // THEN
        ArgumentCaptor<OrbitResult> captor = ArgumentCaptor.forClass(OrbitResult.class);
        verify(rabbitTemplate).convertAndSend(eq("orbit-results-queue"), captor.capture());

        // Verify that the system triggered the FAIL
        OrbitResult result = captor.getValue();
        assertEquals("FAILED", result.getStatus());
        assertEquals("Critical Error: Not enough fuel for maneuver.", result.getDetails());
    }

    @Test
    void shouldIgnoreDuplicate_WhenIdExists() {
        // GIVEN
        OrderEvent event = new OrderEvent("uuid-dup", "Falcon", "CALCULATE");
        // Simulate IT EXISTs on DB
        when(repository.existsById("uuid-dup")).thenReturn(true);

        // WHEN
        inventoryService.handleOrbitProcess(event);

        // THEN
        // Verify that nothing was saved nor send
        verify(repository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), (Object) any());
    }
    @Test
    void shouldDeleteFromMongo_WhenActionIsRollback() {

        String id = "uuid-123";
        OrderEvent rollbackEvent = new OrderEvent(id, "Sentinel-6", "ROLLBACK_ORBIT");

        when(repository.existsById(id)).thenReturn(false, true);

        inventoryService.handleOrbitProcess(rollbackEvent);

        // THEN
        verify(repository, times(1)).deleteById(id);
        System.out.println("Verified: Repository delete was called for rollback");

    }
}
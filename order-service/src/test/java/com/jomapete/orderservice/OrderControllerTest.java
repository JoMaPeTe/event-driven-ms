package com.jomapete.orderservice;

import com.jomapete.orderservice.controller.OrderController;
import com.jomapete.orderservice.dto.OrderEvent;
import com.jomapete.orderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

        @Mock
        private OrderServiceImpl orderService; // Mocking the Service Interface

        @InjectMocks
        private OrderController orderController;

        @Test
        void shouldReturnSuccessMessage_WhenOrderIsLaunched() {
            // GIVEN: A simulated request from the user
            OrderEvent request = new OrderEvent();
            request.setSatelliteName("Explorer-1");

            // Mocking the service response to return the event with a generated ID
            OrderEvent mockProcessedEvent = new OrderEvent();
            mockProcessedEvent.setId("mock-uuid-123");
            mockProcessedEvent.setSatelliteName("Explorer-1");

            when(orderService.createOrder(any(OrderEvent.class))).thenReturn(mockProcessedEvent);

            // WHEN: Calling the controller endpoint
            String response = orderController.launchSatelliteTask(request);

            // THEN:
            // 1. Verify that the controller delegated the responsibility to the Service
            verify(orderService, times(1)).createOrder(request);

            // 2. Verify that the HTTP response string contains key data
            assertTrue(response.contains("Explorer-1"));
            assertTrue(response.contains("mock-uuid-123"));
        }
}
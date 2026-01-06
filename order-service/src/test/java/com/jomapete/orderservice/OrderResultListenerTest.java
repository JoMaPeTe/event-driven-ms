package com.jomapete.orderservice;

import com.jomapete.orderservice.dto.OrbitResult;
import com.jomapete.orderservice.consumer.OrderResultListener;
import com.jomapete.orderservice.entity.OrderState;
import com.jomapete.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderResultListenerTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderResultListener resultListener;

    @Test
    void shouldCompleteSaga_WhenResultIsSuccess() {
        // GIVEN (A pending order exists in DB)
        String orderId = "uuid-123";
        OrderState existingOrder = new OrderState(orderId, "PENDING");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // WHEN (We receive a COMPLETED event from Inventory)
        OrbitResult result = new OrbitResult(orderId, "COMPLETED", "OK");
        resultListener.handleResult(result);

        // THEN (Status should update to COMPLETED)
        verify(orderRepository).save(argThat(order ->
                order.getStatus().equals("COMPLETED")
        ));
    }

    @Test
    void shouldTriggerRollback_WhenResultIsFailed() {
        // GIVEN (A pending order exists in DB)
        String orderId = "uuid-error";
        OrderState existingOrder = new OrderState(orderId, "PENDING");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // WHEN (We receive a FAILED event from Inventory)
        OrbitResult result = new OrbitResult(orderId, "FAILED", "Not enough fuel");
        resultListener.handleResult(result);

        // THEN (Compensating Transaction: Status must be CANCELLED)
        verify(orderRepository).save(argThat(order ->
                order.getStatus().equals("CANCELLED")
        ));

        System.out.println(" TEST PASSED: Saga Rollback triggered correctly.");
    }
}
package com.jomapete.orderservice;

import com.jomapete.orderservice.consumer.OrderResultListener;
import com.jomapete.orderservice.dto.OrbitResult;
import com.jomapete.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderResultListenerTest {

    @Mock
    private OrderService orderService; // Mockeamos la INTERFAZ

    @InjectMocks
    private OrderResultListener resultListener;

    @Test
    void shouldDelegateToService_WhenResultReceived() {
        // GIVEN
        OrbitResult result = new OrbitResult("uuid-123", "COMPLETED", "OK");

        // WHEN
        resultListener.handleResult(result);

        // THEN
        verify(orderService, times(1)).processOrbitResult(result);
    }
}
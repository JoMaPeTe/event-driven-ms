package com.jomapete.orderservice;

import com.jomapete.orderservice.controller.OrderController;
import com.jomapete.orderservice.dto.OrderEvent;
import com.jomapete.orderservice.entity.OrderState;
import com.jomapete.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private RabbitTemplate rabbitTemplate; // Mock RabbitMQ

    @Mock
    private OrderRepository orderRepository; // Mock MongoDB

    @InjectMocks
    private OrderController orderController; // System Under Test

    @Test
    void shouldCreateOrder_AndPublishMessage() {
        // GIVEN
        OrderEvent request = new OrderEvent();
        request.setSatelliteName("Falcon-9");
        // No action set, testing default value logic

        // WHEN
        String response = orderController.launchSatelliteTask(request);

        // THEN
        // 1. Verify Persistence (Database per Service pattern)
        ArgumentCaptor<OrderState> orderCaptor = ArgumentCaptor.forClass(OrderState.class);
        verify(orderRepository).save(orderCaptor.capture());

        OrderState savedOrder = orderCaptor.getValue();
        assertEquals("PENDING", savedOrder.getStatus()); // Must start as PENDING
        assertNotNull(savedOrder.getId()); // ID must be generated

        // 2. Verify Producer (Message Sending)
        verify(rabbitTemplate).convertAndSend(eq("orbit-tasks-queue"), any(OrderEvent.class));

        System.out.println("TEST PASSED: Order saved as PENDING and published to Queue.");
    }
}
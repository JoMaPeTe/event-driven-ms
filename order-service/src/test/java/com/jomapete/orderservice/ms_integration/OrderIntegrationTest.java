package com.jomapete.orderservice.ms_integration;

import com.jomapete.orderservice.OrderServiceApplication;
import com.jomapete.orderservice.dto.OrderEvent;
import com.jomapete.orderservice.entity.OrderState;
import com.jomapete.orderservice.repository.OrderRepository;
import com.jomapete.orderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = OrderServiceApplication.class)
@Testcontainers
class OrderIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.11-management");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    // Inyectamos los componentes REALES gestionados por Spring
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderServiceImpl orderService;

    @Test
    void shouldAutoCancel_WhenInventoryIsDown() {
        // GIVEN: Creamos una orden real que se guardará en el MongoDB del contenedor
        OrderEvent request = new OrderEvent();
        request.setSatelliteName("Voyager-1");
        OrderEvent created = orderService.createOrder(request);

        // THEN: Verificamos que el sistema (vía RabbitMQ TTL) actualice el estado
        await()
                .atMost(15, SECONDS)
                .pollInterval(1, SECONDS) // Revisa la DB cada segundo
                .untilAsserted(() -> {
                    // Buscamos directamente en la base de datos real
                    OrderState state = orderRepository.findById(created.getId())
                            .orElseThrow(() -> new RuntimeException("Order not found in DB"));

                    assertEquals("CANCELLED_BY_TIMEOUT", state.getStatus());
                });
    }
}
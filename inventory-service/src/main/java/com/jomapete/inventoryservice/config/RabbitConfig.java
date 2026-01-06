package com.jomapete.inventoryservice.config;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // QUEUE TO LISTEN; SAME NAME AS IN  OrderController
    @Bean
    public Queue tasksQueue() {
        return new Queue("orbit-tasks-queue", true);
    }

    // QUEUE TO SEND
    @Bean
    public Queue resultsQueue() {
        return new Queue("orbit-results-queue", true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}





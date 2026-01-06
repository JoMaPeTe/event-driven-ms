package com.jomapete.orderservice.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {
    // QUEUE TO LISTEN
    @Bean
    public Queue resultsQueue() {
        // nombre, durable (true para que no se borre si reinicias Rabbit)
        return new Queue("orbit-results-queue", true);
    }

    // 2. QUEUE TO SEND
    @Bean
    public Queue tasksQueue() {
        return new Queue("orbit-tasks-queue", true);
    }
    /**
    *This bean send pretty JSONs instead of bytes
    */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}


package com.jomapete.orderservice.config;

import org.springframework.amqp.core.*;
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
        return QueueBuilder.durable("orbit-tasks-queue")
                .withArgument("x-dead-letter-exchange", "order-timeout-exchange")
                .withArgument("x-dead-letter-routing-key", "timeout")
                .withArgument("x-message-ttl", 10000) // 10 seconds of live
                .build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("order-timeout-exchange");
    }
    @Bean
    public Queue timeoutQueue() {
        return new Queue("order-timeout-queue", true);
    }

    @Bean
    public Binding timeoutBinding(Queue timeoutQueue, DirectExchange timeoutExchange) {
        return BindingBuilder.bind(timeoutQueue).to(timeoutExchange).with("timeout");
    }


    /**
    *This bean send pretty JSONs instead of bytes
    */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}


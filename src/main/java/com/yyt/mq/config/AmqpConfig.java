package com.yyt.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {


    public final static String queueName = "queue-order";

    public final static String exchangeName = "exchange-order";

    public final static String routingKeyName = "key-order";

    @Bean
    public Queue queueMessage() {
        return new Queue(AmqpConfig.queueName);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(AmqpConfig.exchangeName);
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with(AmqpConfig.routingKeyName);
    }

}

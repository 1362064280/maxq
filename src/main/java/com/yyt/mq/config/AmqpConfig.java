package com.yyt.mq.config;

import com.yyt.mq.constant.MQConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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

    @Bean
    public Queue stockDeadLetterQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", MQConstant.STOCK_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", MQConstant.STOCK_REPEAT_TRADE_QUEUE_NAME);
        Queue queue = new Queue(MQConstant.STOCK_DEAD_LETTER_QUEUE_NAME,true,false,false,arguments);
        System.out.println("arguments :" + queue.getArguments());
        return queue;
    }

    @Bean
    public DirectExchange stockExchange() {
        return new DirectExchange(MQConstant.STOCK_EXCHANGE, true, false);
    }

    @Bean
    public Binding  deadLetterBinding() {
        return BindingBuilder.bind(stockDeadLetterQueue()).to(stockExchange()).with(MQConstant.STOCK_DEAD_LETTER_QUEUE_NAME);
    }

    @Bean
    public Queue repeatTradeQueue() {
        Queue queue = new Queue(MQConstant.STOCK_REPEAT_TRADE_QUEUE_NAME,true,false,false);
        return queue;
    }

    @Bean
    public Binding  drepeatTradeBinding() {
        return BindingBuilder.bind(repeatTradeQueue()).to(stockExchange()).with(MQConstant.STOCK_REPEAT_TRADE_QUEUE_NAME);
    }


}

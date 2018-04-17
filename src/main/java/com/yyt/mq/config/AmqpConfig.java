package com.yyt.mq.config;

import com.yyt.mq.constant.MQConstant;
import com.yyt.mq.listener.OrderMessageListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AmqpConfig {

    @Bean
    public Queue orderQueue() {
        return new Queue(MQConstant.ORDER_QUEUE);
    }

    @Bean
    TopicExchange orderExchange() {
        return new TopicExchange(MQConstant.ORDER_EXCHANGE);
    }

    @Bean
    Binding orderBind(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(MQConstant.ORDER_ROUTING_KEY);
    }

    @Autowired
    private OrderMessageListener orderMessageListener;

    @Bean
    SimpleMessageListenerContainer getQueueMessageListenerContainer(ConnectionFactory connectionFactory, Queue orderQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(orderQueue);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        /**
         * 设置确认模式手工确认
         */
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(orderMessageListener);
        return container;
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

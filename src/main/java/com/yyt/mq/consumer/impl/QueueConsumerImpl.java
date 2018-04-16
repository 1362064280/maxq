package com.yyt.mq.consumer.impl;

import com.yyt.mq.config.AmqpConfig;
import com.yyt.mq.consumer.AMQPConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component("queueConsumer")
@RabbitListener(queues = AmqpConfig.queueName)
public class QueueConsumerImpl implements AMQPConsumer {

    @Override
    @RabbitHandler
    public void receive(Object obj) {
        System.out.println( Thread.currentThread().getName() + "接收消息<<<<<<<<  : " + obj);
    }

}

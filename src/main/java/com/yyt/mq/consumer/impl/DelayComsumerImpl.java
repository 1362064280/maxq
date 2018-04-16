package com.yyt.mq.consumer.impl;

import com.yyt.mq.constant.MQConstant;
import com.yyt.mq.consumer.AMQPConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RabbitListener(queues = MQConstant.STOCK_REPEAT_TRADE_QUEUE_NAME)
public class DelayComsumerImpl implements AMQPConsumer{

    @RabbitHandler
    @Override
    public void receive(String content) {
        System.out.println(LocalDateTime.now() + "延迟消息接收<<<<<<<<<" + content);
    }
}
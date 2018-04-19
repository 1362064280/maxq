package com.yyt.mq.consumer.impl;

import com.yyt.mq.constant.MQConstant;
import com.yyt.mq.consumer.AMQPConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RabbitListener(queues = MQConstant.STOCK_REPEAT_TRADE_QUEUE_NAME)
public class DeadLetterComsumerImpl implements AMQPConsumer{

    private static Logger logger = LoggerFactory.getLogger(DeadLetterComsumerImpl.class);

    @RabbitHandler
    @Override
    public void receive(String content) throws Exception  {
        logger.info("{} 延迟接收到的消息<<<<<<<<<", LocalDateTime.now(), content);
    }
}
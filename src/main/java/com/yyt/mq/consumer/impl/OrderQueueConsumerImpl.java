package com.yyt.mq.consumer.impl;

import com.yyt.mq.consumer.AMQPConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("orderQueueConsumer")
public class OrderQueueConsumerImpl implements AMQPConsumer {

    private static Logger logger = LoggerFactory.getLogger(OrderQueueConsumerImpl.class);

    @Override
    public void receive(String content) throws Exception {
        logger.info("{} 接收消息订单消息<<<<<<<< {}", Thread.currentThread().getName(), content);
    }

}

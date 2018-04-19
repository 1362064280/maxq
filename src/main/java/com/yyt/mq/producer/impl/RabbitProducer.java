package com.yyt.mq.producer.impl;

import com.yyt.mq.util.Rabbit;
import com.yyt.mq.util.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitProducer {

    private static Logger logger = LoggerFactory.getLogger(RabbitProducer.class);

    private Rabbit rabbit = null;

    // TODO 初始化发送端属性配置
    public RabbitProducer(RabbitConfig rabbitConfig) {
        rabbit = new Rabbit();
        rabbit.init(rabbitConfig);
    }

    public void send(String routingKey, Object message) {
        logger.info("send   routingKey:{}, message:{}" , routingKey, message.toString());
        rabbit.send(routingKey, message);
    }

}

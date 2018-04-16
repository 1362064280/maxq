package com.yyt.mq.producer.impl;

import com.yyt.mq.config.AmqpConfig;
import com.yyt.mq.producer.AMQPProduce;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("queueProduce")
public class QueueProduceImpl implements AMQPProduce {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void send(Object obj) {
        System.out.println("发送消息>>>>>>" + obj.toString());
        try {
            this.rabbitTemplate.convertAndSend(AmqpConfig.exchangeName, AmqpConfig.routingKeyName, obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

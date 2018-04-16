package com.yyt.mq.producer.impl;

import com.yyt.mq.config.AmqpConfig;
import com.yyt.mq.constant.MQConstant;
import com.yyt.mq.help.DLXMessage;
import com.yyt.mq.help.MapperHelper;
import com.yyt.mq.producer.AMQPProduce;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("queueProduce")
public class QueueProduceImpl implements AMQPProduce {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void send(String exchange, String routingKey, String message) {
        try {
            this.rabbitTemplate.convertAndSend(AmqpConfig.exchangeName, AmqpConfig.routingKeyName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDelay(String exchange, String queueName, String message, long times) {
        MessagePostProcessor postProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(times + "");
                return message;
            }
        };
        rabbitTemplate.convertAndSend(MQConstant.STOCK_EXCHANGE, MQConstant.STOCK_DEAD_LETTER_QUEUE_NAME, message, postProcessor);
    }

}

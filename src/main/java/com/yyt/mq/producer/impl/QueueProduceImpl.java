package com.yyt.mq.producer.impl;

import com.yyt.mq.constant.MQConstant;
import com.yyt.mq.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("queueProduce")
public class QueueProduceImpl implements Producer {

    private static Logger logger = LoggerFactory.getLogger(QueueProduceImpl.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(String exchange, String routingKey, String message) {
        try {
            logger.info("及时发送的消息>>>>>{}" , message);
            this.rabbitTemplate.convertAndSend(exchange, routingKey, message);
        } catch (Exception e) {
            logger.info("及时发送的消息失败>>>>>{}" , e.getMessage());
        }
    }

    @Override
    public void sendDelay(String exchange, String queueName, String message, long times) {
        try {
            MessagePostProcessor postProcessor = new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setExpiration(times + "");
                    return message;
                }
            };
            logger.info("延时发送的消息>>>>>{}" , message);
            rabbitTemplate.convertAndSend(MQConstant.STOCK_EXCHANGE, MQConstant.STOCK_DEAD_LETTER_QUEUE_NAME, message, postProcessor);
        } catch (Exception e) {
            logger.info("延时发送的消息失败>>>>>{}" , e.getMessage());
        }

    }

}

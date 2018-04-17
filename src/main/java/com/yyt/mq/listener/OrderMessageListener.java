package com.yyt.mq.listener;

import com.rabbitmq.client.Channel;
import com.yyt.mq.constant.MQConstant;
import com.yyt.mq.consumer.AMQPConsumer;
import com.yyt.mq.help.MapperHelper;
import com.yyt.mq.model.Order;
import com.yyt.mq.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("orderMessageListener")
public class OrderMessageListener implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(OrderMessageListener.class);

    @Autowired
    private AMQPConsumer orderQueueConsumer;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String body = new String(message.getBody(), MQConstant.CHARSET);
            logger.info("收到的消息<<<<<<< {}", body);
            orderQueueConsumer.receive(body);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            logger.info("接收消息异常: {}", e.getMessage());
        }
    }
}

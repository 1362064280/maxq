package com.yyt.mq.listener;

import com.rabbitmq.client.Channel;
import com.yyt.mq.constant.MQConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component("delayMessageListener")
public class DelayMessageListener implements ChannelAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(DelayMessageListener.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        try {

        } catch (Exception e) {
            int retryCount = (int)message.getMessageProperties().getHeaders().getOrDefault(MQConstant.RETRY_COUNT, 1);
            if(retryCount < 3) {

            }
        }

    }
}

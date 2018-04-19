package com.yyt.mq.consumer.impl;

import com.yyt.mq.consumer.Consumer;
import com.yyt.mq.util.ConsumerConfig;
import com.yyt.mq.util.Message;
import com.yyt.mq.util.Rabbit;
import com.yyt.mq.util.RabbitConfig;
import lombok.Data;

@Data
public class RabbitConsumer implements Consumer {

    private Rabbit rabbit = null;

    private ConsumerConfig consumerConfig;

    public RabbitConsumer(ConsumerConfig config, RabbitConfig rabbitConfig){
        rabbit = new Rabbit();
        rabbit.init(rabbitConfig);
        rabbit.setAutoAck(config.getAutoAck());
        this.consumerConfig = config;
    }

    @Override
    public Message fetch() {
        return fetch(consumerConfig.getFetchTimeoutMills());
    }

    @Override
    public Message fetch(int waitMills) {
        return rabbit.fetch(waitMills);
    }

    @Override
    public void ack(Message message) {
        rabbit.ack(message);
    }

    @Override
    public void nack(Message message) {
        rabbit.nack(message);
    }
}

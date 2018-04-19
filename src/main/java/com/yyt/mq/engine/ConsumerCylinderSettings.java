package com.yyt.mq.engine;

import com.yyt.mq.consumer.AMQPConsumer;
import com.yyt.mq.consumer.Consumer;
import lombok.Data;

@Data
public class ConsumerCylinderSettings {

    private String name;

    private Class<? extends Consumer> messageConsumerType;

    private Integer workerPoolSize;

    private Class<? extends AMQPConsumer> handler;

}

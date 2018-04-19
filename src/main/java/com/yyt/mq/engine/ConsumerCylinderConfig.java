package com.yyt.mq.engine;

import com.yyt.mq.consumer.AMQPConsumer;
import com.yyt.mq.consumer.Consumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerCylinderConfig {

    private static final int DEFAULT_WORKER_POOL_SIZE = 200;

    private String name;
    private Class<? extends Consumer> messageConsumerType;
    private Integer workerPoolSize;
    private Class<? extends AMQPConsumer> handler;



    public static ConsumerCylinderConfig of(ConsumerCylinderSettings consumerCylinderSettings) {
        return new ConsumerCylinderConfig(
                consumerCylinderSettings.getName(),
                consumerCylinderSettings.getMessageConsumerType(),
                consumerCylinderSettings.getWorkerPoolSize() == -1 ? DEFAULT_WORKER_POOL_SIZE : consumerCylinderSettings.getWorkerPoolSize(),
                consumerCylinderSettings.getHandler()
        );
    }


}

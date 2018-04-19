package com.yyt.mq.util;


import com.yyt.mq.constant.MQConstant;
import com.yyt.mq.consumer.impl.OrderQueueConsumerImpl;
import com.yyt.mq.consumer.impl.RabbitConsumer;
import com.yyt.mq.engine.ConsumerCylinderSettings;

import java.util.Arrays;
import java.util.List;

/**
 * 用来做关系配置
 */
public class RabbitProcessor {


    // TODO 后期会通过注解的形式来进行配置

    /**初始化绑定信息
     *@RabbitSettings(
        exchangeName = "biz.shopping.core",
        queueName = "advertisement_click_event",
        routingKey = "biz.shopping.banner.business_ad.click"
        )
     *
     */
    public static RabbitConfig getRabbitConfig1(){
        String url = "amqp://guest:guest@127.0.0.1:5672//";
        String exchangeName = MQConstant.ORDER_EXCHANGE+"-new";
        List<String> routingKeys = Arrays.asList(MQConstant.ORDER_ROUTING_KEY+"new1", MQConstant.ORDER_ROUTING_KEY+"new2", MQConstant.ORDER_ROUTING_KEY+"new3");
        String queueName = MQConstant.ORDER_QUEUE+"-new";
        RabbitConfig rabbitConfig = new RabbitConfig(url, exchangeName, routingKeys, queueName);
        return rabbitConfig;
    }

    public static ConsumerCylinderSettings getConsumerCylinderSettings1(){
        ConsumerCylinderSettings settings = new ConsumerCylinderSettings();
        settings.setName("orderService");
        settings.setWorkerPoolSize(200);
        settings.setMessageConsumerType(RabbitConsumer.class);
        settings.setHandler(OrderQueueConsumerImpl.class);
        return settings;
    }

    public static ConsumerConfig getConsumerConfig1() {
        ConsumerConfig config = new ConsumerConfig();
        config.setAutoAck(false);
        config.setFetchTimeoutMills(-1);
        return config;
    }


}

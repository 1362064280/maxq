package com.yyt.mq.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 路由属性配置
 */
@Data
@AllArgsConstructor
public class RabbitConfig {

    //amqp://userName:password@hostName:portNumber/virtualHost
    private String url;
    private String exchangeName;
    private List<String> routingKeys;
    private String queueName;

    public static RabbitConfig of(String url, String exchangeName, List<String> routingKeys, String queueName){
        return new RabbitConfig(url, exchangeName, routingKeys, queueName);
    }

}

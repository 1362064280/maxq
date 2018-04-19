package com.yyt.mq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.netty.channel.ConnectTimeoutException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 通信属性封装
 */
@Data
public class RabbitClient {

    private Logger logger = LoggerFactory.getLogger(RabbitClient.class);

    private ConnectionFactory connectionFactory;
    private volatile Connection connection;
    private volatile Channel channel;

    public RabbitClient(ConnectionFactory connectionFactory) throws IOException{
        this.connectionFactory = connectionFactory;
        createChannel(createConnection(this.connectionFactory));
        startRepairMan();
    }

    private Connection createConnection(ConnectionFactory connectionFactory) throws IOException {
        if(null != this.connection && connection.isOpen()) {
            return connection;
        }
        try {
            this.connection = connectionFactory.newConnection();
        } catch (TimeoutException e) {
            throw new ConnectTimeoutException("RabbitConfig.url new connection time out");
        }
        return this.connection;
    }

    private void createChannel(Connection connection) throws IOException {
        if(null != channel && channel.isOpen()) {
            return;
        }
        this.channel = connection.createChannel();
    }

    private void startRepairMan() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    createChannel(createConnection(this.connectionFactory));
                } catch (IOException e) {
                    logger.error("Faild to fix channel", e);
                }
            }
        }).start();
    }


}

package com.yyt.mq.util;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yyt.mq.exception.UnexpectedStateException;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class RabbitUtil {

    private static final String DEFAULT_EXCHANGE_TYPE = "topic";
    private static final boolean DEFAULT_DURABLE = true;
    private static final boolean DEFAULT_EXCLUSIVE = false;
    private static final boolean DEFAULT_AUTO_DELETE = false;


    /**
     * 构建通信对象
     * @param rabbitConfig
     * @return
     */
    public static RabbitClient createClient(RabbitConfig rabbitConfig) {
        ConnectionFactory connectionFactory = null;
        try {
            connectionFactory = createConnectionFactory(rabbitConfig);
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalArgumentException("Wrong format for RabbitConfig.url");
        }

        try {
            RabbitClient rabbitClient = new RabbitClient(connectionFactory);
            initQueue(rabbitClient.getConnection(), rabbitClient.getChannel(), rabbitConfig);
            return rabbitClient;
        } catch (IOException e) {
            throw new IllegalStateException("Create rabbitmq connection failed.", e);
        }
    }

    /**
     * 初始化链接工厂
     * @param rabbitConfig
     * @return
     * @throws URISyntaxException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static ConnectionFactory createConnectionFactory(RabbitConfig rabbitConfig) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置网络异常重连
        connectionFactory.setAutomaticRecoveryEnabled(true);
        //60秒
        connectionFactory.setRequestedHeartbeat(60);
        //2秒
        connectionFactory.setConnectionTimeout(2000);
        return connectionFactory;
    }

    /**
     * 初始化队列信息
     * @param connection
     * @param channel
     * @param rabbitConfig
     * @throws IOException
     */
    private static void initQueue(Connection connection, Channel channel, RabbitConfig rabbitConfig) throws IOException {
        //交换器
        if(!isExchangeExists(connection, rabbitConfig.getExchangeName())) {
            channel.exchangeDeclare(rabbitConfig.getExchangeName(), DEFAULT_EXCHANGE_TYPE, DEFAULT_DURABLE);
        }
        if(!StringUtils.isEmpty(rabbitConfig.getQueueName()) && !rabbitConfig.getRoutingKeys().isEmpty()) {
            //队列
            if(!isQueueExists(connection, rabbitConfig.getQueueName())) {
                channel.queueDeclare(rabbitConfig.getQueueName(), DEFAULT_DURABLE, DEFAULT_EXCLUSIVE, DEFAULT_AUTO_DELETE, null);
            }
            //绑定
            for(String routingKey : rabbitConfig.getRoutingKeys()) {
                channel.queueBind(rabbitConfig.getQueueName(), rabbitConfig.getExchangeName(), routingKey);
            }
        }
    }

    /**
     * 判断交换器是否存在
     * @param connection
     * @param exchangeName
     * @return
     */
    private static boolean isExchangeExists(Connection connection, String exchangeName) {
        try {
            Channel channel = connection.createChannel();
            //可以使用该函数使用一个已经建立的exchange
            channel.exchangeDeclarePassive(exchangeName);
            channel.close();
            return true;
        } catch (IOException e) {
            return false;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * 判断队列是否存在
     * @param connection
     * @param queueName
     * @return
     */
    private static boolean isQueueExists(Connection connection, String queueName) {
        Channel channel = null;
        try {
            channel = connection.createChannel();
            channel.queueDeclarePassive(queueName);
            channel.close();
            return true;
        } catch (IOException e) {
            return false;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @FunctionalInterface
    public interface ChannelConsumer{
        void accept(Channel channel) throws IOException;
    }

    public interface ChannelFunction<A> {
        A apply(Channel channel) throws IOException;
    }

    @AllArgsConstructor
    public static  class ChannelHolder {
        private Channel channel;

        public void consume(ChannelConsumer channelConsumer) {
            try {
                channelConsumer.accept(channel);
            } catch (IOException e) {
                throw new UnexpectedStateException(e);
            }
        }

        public <A> A apply(ChannelFunction<A> function) {
            try {
                return function.apply(channel);
            } catch (IOException e) {
                throw new UnexpectedStateException(e);
            }
        }
    }

    public static ChannelHolder with(Channel channel) {
        return new ChannelHolder(channel);
    }

}

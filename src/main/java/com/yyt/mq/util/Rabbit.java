package com.yyt.mq.util;

import com.google.common.collect.Maps;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.GetResponse;
import com.yyt.mq.exception.UnexpectedStateException;
import com.yyt.mq.help.MapperHelper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.JmsProperties;

import java.util.Map;

/**
 * 消息工具
 */
@Data
public class Rabbit {

    private Logger logger = LoggerFactory.getLogger(Rabbit.class);

    private RabbitClient rabbitClient;

    private RabbitConfig rabbitConfig;

    private boolean autoAck = false;

    private static final boolean DEFAULT_MANDATORY = false;
    private static final boolean DEFAULT_MULTIPLE = false;
    private static final boolean DEFAULT_REQUEUE = true;
    private static final int DEFAULT_WAIT_DENOMINATOR = 10;

    private final Map<Object, Long> messageMap = Maps.newIdentityHashMap();


    public void init(RabbitConfig rabbitConfig) {
        this.rabbitConfig = rabbitConfig;
        this.rabbitClient = RabbitUtil.createClient(rabbitConfig);
    }

    public void setConsumerMode(boolean autoAck) {
        this.autoAck = autoAck;
    }

    public void send(String routingKey, Object message) {
        if(null != message) {
            String messageJson = MapperHelper.writeValueAsString(message);
            RabbitUtil.with(rabbitClient.getChannel()).consume(channel -> {
                /**
                 * mandatory：当mandatory标志位设置为true时，如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，那么会调用basic.return方法将消息返回给生产者（Basic.Return + Content-Header + Content-Body）；当mandatory设置为false时，出现上述情形broker会直接将消息扔掉。
                 * immediate：当immediate标志位设置为true时，如果exchange在将消息路由到queue(s)时发现对于的queue上么有消费者，那么这条消息不会放入队列中。当与消息routeKey关联的所有queue（一个或者多个）都没有消费者时，该消息会通过basic.return方法返还给生产者。
                 */
                AMQP.BasicProperties.Builder builder= new AMQP.BasicProperties().builder();
                //1:nonpersistent 2:persistent
                builder.deliveryMode(JmsProperties.DeliveryMode.PERSISTENT.getValue());
                AMQP.BasicProperties properties = builder.build();
                channel.basicPublish(rabbitConfig.getExchangeName(), routingKey, DEFAULT_MANDATORY, properties, messageJson.getBytes() );
            });
        }
    }

    private GetResponse doFetch(int waitMills) {
        int waitedMills = 0;
        int waitIntervalMills = (int) Math.ceil(waitMills / DEFAULT_WAIT_DENOMINATOR);
        GetResponse response = null;
        try {
            RabbitUtil.ChannelHolder channelHolder = RabbitUtil.with(rabbitClient.getChannel());
            response = channelHolder.apply(channel -> channel.basicGet(rabbitConfig.getQueueName(), autoAck));
            while (null == response && waitedMills < waitMills) {
                Thread.sleep(waitIntervalMills);
                waitedMills += waitIntervalMills;
                response = channelHolder.apply(channel -> channel.basicGet(rabbitConfig.getQueueName(), autoAck));
            }
        } catch (AlreadyClosedException ex) {
            logger.error("Connection to rabbit in [%s] closed unexpectedly.", getClass());
        } catch (InterruptedException e) {
            throw new UnexpectedStateException(e);
        }
        return response;
    }

    public Message fetch(int waitMills) {
        GetResponse response = doFetch(waitMills);
        if(null == response) {
            return null;
        }
        Message message = new Message();
        message.setRoutingKey(response.getEnvelope().getRoutingKey());
        message.setPayloadStrign(new String(response.getBody()));
        if(!autoAck) {
            synchronized (messageMap) {
                messageMap.put(message, response.getEnvelope().getDeliveryTag());
            }
        }
        return message;
    }

    public void ack(Message message) {
        if(null != message && messageMap.containsKey(message)) {
            RabbitUtil.with(rabbitClient.getChannel()).consume(channel -> channel.basicAck(messageMap.get(message), DEFAULT_MULTIPLE));
            synchronized (messageMap) {
                messageMap.remove(message);
            }
        }
    }

    public void nack(Message message) {
        if(null != message && messageMap.containsKey(message)) {
            RabbitUtil.with(rabbitClient.getChannel()).consume(channel -> channel.basicNack(messageMap.get(message), DEFAULT_MULTIPLE, DEFAULT_REQUEUE));
            synchronized (messageMap) {
                messageMap.remove(message);
            }
        }
    }

}

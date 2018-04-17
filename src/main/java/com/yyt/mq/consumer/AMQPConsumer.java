package com.yyt.mq.consumer;

public interface AMQPConsumer {

    public void receive(String content) throws Exception;

}

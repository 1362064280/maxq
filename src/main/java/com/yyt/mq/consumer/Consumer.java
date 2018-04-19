package com.yyt.mq.consumer;

import com.yyt.mq.util.Message;

public interface Consumer {

    Message fetch();

    Message fetch(int waitMills);

    void ack(Message message);

    void nack(Message message);

}

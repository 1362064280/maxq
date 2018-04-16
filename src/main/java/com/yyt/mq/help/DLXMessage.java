package com.yyt.mq.help;

import lombok.Data;

@Data
public class DLXMessage {

    private String exchange;

    private String queueName;

    private String content;

    private long times;

    public DLXMessage() {
        super();
    }

    public DLXMessage(String queueName, String content, long times) {
        super();
        this.queueName = queueName;
        this.content = content;
        this.times = times;
    }

    public DLXMessage(String exchange, String queueName, String content, long times) {
        super();
        this.exchange = exchange;
        this.queueName = queueName;
        this.content = content;
        this.times = times;
    }

    @Override
    public String toString() {
        return "DLXMessage{" +
                "exchange='" + exchange + '\'' +
                ", queueName='" + queueName + '\'' +
                ", content='" + content + '\'' +
                ", times=" + times +
                '}';
    }
}

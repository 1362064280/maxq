package com.yyt.mq.producer;

public interface AMQPProduce {

    /**
     * 发送消息到队列
     * @param exchange 交换器
     * @param queueName 队列名称
     * @param message 消息内容
     */
    public void send(String exchange, String queueName, String message);

    /**
     * 延迟发送消息到队列
     * @param exchange 交换器
     * @param queueName 队列名称
     * @param message 消息内容
     * @param times 延迟时间 单位毫秒
     */
    public void sendDelay(String exchange, String queueName, String message, long times);

}

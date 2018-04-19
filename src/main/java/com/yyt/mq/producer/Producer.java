package com.yyt.mq.producer;

public interface Producer {

    /**
     * 发送消息到队列
     * @param exchange 交换器
     * @param routingKey 路由
     * @param message 消息内容
     */
    public void send(String exchange, String routingKey, String message);

    /**
     * 延迟发送消息到队列
     * @param exchange 交换器
     * @param routingKey 路由
     * @param message 消息内容
     * @param times 延迟时间 单位毫秒
     */
    public void sendDelay(String exchange, String routingKey, String message, long times);

}

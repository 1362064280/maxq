package com.yyt.mq.constant;

/**
 * 消息队列相关常量
 */
public class MQConstant {

    public static final String CHARSET = "utf-8";

    /**
     * 库存的队列，以及死信队列 =====> 延迟发送
     */
    public static final String STOCK_EXCHANGE = "exchange-stock";
    public static final String STOCK_DEAD_LETTER_QUEUE_NAME = "stock.dead.letter.queue";
    public static final String STOCK_REPEAT_TRADE_QUEUE_NAME = "stock.repeat.trade.queue";


    /**
     * 订单的队列
     */
    public static final String ORDER_EXCHANGE = "exchange-order";
    public static final String ORDER_ROUTING_KEY = "key-order";
    public static final String ORDER_QUEUE = "queue-order";


    /**
     * 重试队列
     */
    public static final String RETRY_COUNT = "retry_count";

}

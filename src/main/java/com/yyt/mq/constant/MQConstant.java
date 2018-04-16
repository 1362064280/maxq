package com.yyt.mq.constant;

/**
 * 消息队列相关常量
 */
public class MQConstant {

    private MQConstant(){
    }

    public static final String STOCK_EXCHANGE = "exchange-stock";

    public static final String STOCK_DEAD_LETTER_QUEUE_NAME = "stock.dead.letter.queue";

    public static final String STOCK_REPEAT_TRADE_QUEUE_NAME = "stock.repeat.trade.queue";

}

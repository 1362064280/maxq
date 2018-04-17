package com.yyt.mq;

import com.yyt.mq.config.AmqpConfig;
import com.yyt.mq.constant.MQConstant;
import com.yyt.mq.model.Order;
import com.yyt.mq.model.Stock;
import com.yyt.mq.producer.AMQPProduce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private AMQPProduce queueProduce;

    @Override
    public void run(String... args) throws Exception {

        for(int i=0; i<10; i++) {
            Order order = new Order();
            order.setId(i);
            order.setOrderNo(i+"");
            order.setProductId(i);
            order.setCreateTime(LocalDateTime.now());
            queueProduce.send(MQConstant.ORDER_EXCHANGE, MQConstant.ORDER_ROUTING_KEY, order.toString());
        }

//        for(int i=0; i<1; i++) {
//            Stock stock = new Stock();
//            stock.setId(i);
//            stock.setProductId(i);
//            stock.setProductId(i);
//            stock.setDateTime(LocalDateTime.now());
//            queueProduce.sendDelay(MQConstant.STOCK_EXCHANGE, MQConstant.STOCK_REPEAT_TRADE_QUEUE_NAME, stock.toString(), 60000);
//        }

    }
}

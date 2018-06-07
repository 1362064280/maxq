package com.yyt.mq;

import com.yyt.mq.constant.MQConstant;
import com.yyt.mq.engine.ConsumerCylinderConfig;
import com.yyt.mq.engine.ConsumerCylinderExecutor;
import com.yyt.mq.model.Order;
import com.yyt.mq.producer.Producer;
import com.yyt.mq.producer.impl.RabbitProducer;
import com.yyt.mq.util.ConsumerConfig;
import com.yyt.mq.util.RabbitConfig;
import com.yyt.mq.util.RabbitProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private Producer queueProduce;

    @Override
    public void run(String... args) throws Exception {

//        for(int i=0; i<10; i++) {
//            Order order = new Order();
//            order.setId(i);
//            order.setOrderNo(i+"");
//            order.setProductId(i);
//            order.setCreateTime(LocalDateTime.now());
//            queueProduce.send(MQConstant.ORDER_EXCHANGE, MQConstant.ORDER_ROUTING_KEY, order.toString());
//        }

//        for(int i=0; i<1; i++) {
//            Stock stock = new Stock();
//            stock.setId(i);
//            stock.setProductId(i);
//            stock.setProductId(i);
//            stock.setProductId(i);
//            stock.setDateTime(LocalDateTime.now());
//            queueProduce.sendDelay(MQConstant.STOCK_EXCHANGE, MQConstant.STOCK_REPEAT_TRADE_QUEUE_NAME, stock.toString(), 60000);
//        }

        RabbitProducer rabbitProducer = new RabbitProducer(RabbitProcessor.getRabbitConfig1());

        for(int i=0; i<10; i++) {
            Order order = new Order();
            order.setId(i);
            order.setOrderNo(i+"");
            order.setProductId(i);
            order.setCreateTime(LocalDateTime.now());
            rabbitProducer.send(MQConstant.ORDER_ROUTING_KEY+"new1", order);
        }


        ConsumerCylinderConfig config = ConsumerCylinderConfig.of(RabbitProcessor.getConsumerCylinderSettings1());
        RabbitConfig rabbitConfig = RabbitProcessor.getRabbitConfig1();
        ConsumerConfig consumerConfig = RabbitProcessor.getConsumerConfig1();
        ConsumerCylinderExecutor consumerCylinderExecutor = new ConsumerCylinderExecutor(config, rabbitConfig, consumerConfig);
        consumerCylinderExecutor.start();

    }
}

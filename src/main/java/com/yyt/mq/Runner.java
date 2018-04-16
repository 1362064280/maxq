package com.yyt.mq;

import com.yyt.mq.model.Order;
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
            queueProduce.send(order);
        }

    }
}

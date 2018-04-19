package com.yyt.mq.engine;

import com.yyt.mq.consumer.AMQPConsumer;
import com.yyt.mq.consumer.Consumer;
import com.yyt.mq.util.ConsumerConfig;
import com.yyt.mq.util.Message;
import com.yyt.mq.util.RabbitConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerCylinderExecutor implements Runnable, ThreadFactory {

    private static final int WORKER_IDLE_SECONDS = 60;
    private static final int POLLER_WAIT_INTERVAL_NANOS = 100_000;

    private Logger logger = LoggerFactory.getLogger(ConsumerCylinderExecutor.class);

    private String name;

    private AtomicInteger workerCounter;

    private ThreadPoolExecutor executor;

    private Consumer consumer;

    private AMQPConsumer handler;

    private Consumer initConsumer(Class<? extends Consumer> type, RabbitConfig rabbitConfig, ConsumerConfig consumerConfig) {
        try {
            Constructor c1 = type.getDeclaredConstructor(ConsumerConfig.class, RabbitConfig.class);
            Object obj = c1.newInstance(consumerConfig, rabbitConfig);
            return (Consumer)obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AMQPConsumer initHandler(Class<? extends AMQPConsumer> type) {
        try {
            Constructor c1 = type.getDeclaredConstructor();
            Object obj = c1.newInstance();
            return (AMQPConsumer)obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ConsumerCylinderExecutor(ConsumerCylinderConfig config, RabbitConfig rabbitConfig, ConsumerConfig consumerConfig) {
        this.name = String.format("consumer-%s", config.getName());
        this.workerCounter = new AtomicInteger(0);
        this.consumer = initConsumer(config.getMessageConsumerType(), rabbitConfig, consumerConfig);
        this.handler = initHandler(config.getHandler());
        this.executor = new ThreadPoolExecutor(
                (int) Math.ceil(config.getWorkerPoolSize() / 4),
                config.getWorkerPoolSize(),
                WORKER_IDLE_SECONDS,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                this,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }


    public void start() {
        new Thread(this, String.format("%s-message-poller", name)).start();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, String.format("%s-worker-%s", name, workerCounter.getAndIncrement()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                polling();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void polling() throws InterruptedException {
        if (executor.getMaximumPoolSize() - executor.getActiveCount() <= 0) {
            Thread.sleep(POLLER_WAIT_INTERVAL_NANOS);
        }
        Message message = null;
        try {
            message = consumer.fetch();
        } catch (Exception e) {
            logger.error("consumer fetch error", e);
        }
        if (message == null) {
            Thread.sleep(0, POLLER_WAIT_INTERVAL_NANOS);
            return;
        }
        executor.execute(new ConsumerTask(message));
    }

    @AllArgsConstructor
    private class ConsumerTask implements Runnable {

        private Message message;

        @Override
        public void run() {
            try {
                // TODO 处理消息==>跳转目标处理类
                handler.receive(message.getPayloadStrign());
                //确认消息
                consumer.ack(message);
            } catch (Throwable throwable) {
                logger.error("ConsumerTask", throwable);
                consumer.nack(message);
            }
        }

    }


}

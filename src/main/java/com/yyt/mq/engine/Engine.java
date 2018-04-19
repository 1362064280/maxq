package com.yyt.mq.engine;

public interface Engine {

    @FunctionalInterface
    interface Handler {
        void handle();
    }

}

package com.yyt.mq.exception;

public class UnexpectedStateException extends RuntimeException{

    public UnexpectedStateException() {
        super("Unexpected exception occurred");
    }

    public UnexpectedStateException(Throwable cause) {
        super("Unexpected exception occurred", cause);
    }

}

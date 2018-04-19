package com.yyt.mq.util;

import lombok.Data;

@Data
public final class Message {
    private String routingKey;
    private String payloadStrign;

}

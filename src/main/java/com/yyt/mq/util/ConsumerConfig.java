package com.yyt.mq.util;

import lombok.Data;

@Data
public class ConsumerConfig {

    private Boolean autoAck = false;

    private Integer fetchTimeoutMills = -1;

}

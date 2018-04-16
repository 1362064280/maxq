package com.yyt.mq.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Order implements Serializable {

    private long id;

    private String orderNo;

    private long productId;

    private LocalDateTime createTime;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", productId=" + productId +
                ", createTime=" + createTime +
                '}';
    }
}

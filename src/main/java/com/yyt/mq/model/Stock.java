package com.yyt.mq.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Stock implements Serializable{

    private long id;
    private long productId;
    private long count;
    private LocalDateTime dateTime;

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", productId=" + productId +
                ", count=" + count +
                ", dateTime=" + dateTime +
                '}';
    }
}

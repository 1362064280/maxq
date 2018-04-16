package com.yyt.mq.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Stock implements Serializable{

    private long id;
    private long productId;
    private long count;

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", productId=" + productId +
                ", count=" + count +
                '}';
    }
}

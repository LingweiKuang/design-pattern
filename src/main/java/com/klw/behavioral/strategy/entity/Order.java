package com.klw.behavioral.strategy.entity;

import lombok.Getter;

@Getter
public class Order {
    final String id;
    final double totalAmount; // 为了简洁使用 double（生产用建议 BigDecimal）
    final User user;

    public Order(String id, double totalAmount, User user) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.user = user;
    }
}

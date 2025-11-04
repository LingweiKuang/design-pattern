package com.klw.behavioral.strategy.discount;

import com.klw.behavioral.strategy.entity.Order;

// 无折扣
public class NoDiscount implements DiscountStrategy {
    public double applyDiscount(Order order) {
        return order.getTotalAmount();
    }

    public String name() {
        return "NoDiscount";
    }
}

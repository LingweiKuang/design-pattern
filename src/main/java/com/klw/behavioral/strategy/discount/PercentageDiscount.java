package com.klw.behavioral.strategy.discount;

import com.klw.behavioral.strategy.entity.Order;

// 百分比折扣（例如 10% off）
public class PercentageDiscount implements DiscountStrategy {
    private final double rate; // 0.10 表示 10%

    public PercentageDiscount(double rate) {
        this.rate = rate;
    }

    public double applyDiscount(Order order) {
        return order.getTotalAmount() * (1.0 - rate);
    }

    public String name() {
        return "PercentageDiscount(" + (rate * 100) + "%)";
    }
}

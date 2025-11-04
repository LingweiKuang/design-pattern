package com.klw.behavioral.strategy.discount;

import com.klw.behavioral.strategy.entity.Order;

// 会员固定减免（比如会员减 20）
public class MemberFixedDiscount implements DiscountStrategy {
    private final double amount;

    public MemberFixedDiscount(double amount) {
        this.amount = amount;
    }

    public double applyDiscount(Order order) {
        double res = order.getTotalAmount() - amount;
        return Math.max(0.0, res);
    }

    public String name() {
        return "MemberFixed-" + amount;
    }
}

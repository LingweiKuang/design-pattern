package com.klw.behavioral.strategy.discount;

import com.klw.behavioral.strategy.entity.Order;

// 满减策略：满 200 减 30 为例（可参数化）
public class FullReductionDiscount implements DiscountStrategy {
    private final double threshold;
    private final double reduce;

    public FullReductionDiscount(double threshold, double reduce) {
        this.threshold = threshold;
        this.reduce = reduce;
    }

    public double applyDiscount(Order order) {
        if (order.getTotalAmount() >= threshold) return order.getTotalAmount() - reduce;
        return order.getTotalAmount();
    }

    public String name() {
        return "FullReduction(" + threshold + "-" + reduce + ")";
    }
}

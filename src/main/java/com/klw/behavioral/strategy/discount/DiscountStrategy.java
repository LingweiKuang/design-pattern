package com.klw.behavioral.strategy.discount;

import com.klw.behavioral.strategy.entity.Order;

// 策略接口
public interface DiscountStrategy {
    // 返回折后金额（非折扣额度）
    double applyDiscount(Order order);

    String name();
}

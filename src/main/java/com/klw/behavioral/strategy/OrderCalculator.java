package com.klw.behavioral.strategy;

import com.klw.behavioral.strategy.discount.DiscountStrategy;
import com.klw.behavioral.strategy.entity.CalculationResult;
import com.klw.behavioral.strategy.entity.Order;

// 结算上下文，只依赖策略接口
public class OrderCalculator {
    private final StrategySelector selector;

    OrderCalculator(StrategySelector selector) {
        this.selector = selector;
    }

    CalculationResult calculate(Order order) {
        DiscountStrategy strategy = selector.select(order);
        double finalAmount = strategy.applyDiscount(order);
        double discount = order.getTotalAmount() - finalAmount;
        return new CalculationResult(order.getId(), order.getTotalAmount(), finalAmount, discount, strategy.name());
    }
}

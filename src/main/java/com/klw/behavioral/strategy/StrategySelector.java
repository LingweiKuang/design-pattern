package com.klw.behavioral.strategy;

import com.klw.behavioral.strategy.discount.*;
import com.klw.behavioral.strategy.entity.Order;

import java.util.ArrayList;
import java.util.List;

// 策略选择器（负责按规则在运行时选出具体策略）
public class StrategySelector {
    private final List<DiscountStrategy> registered = new ArrayList<>();

    // 注册可用策略（可在启动时注册所有实现）
    void register(DiscountStrategy s) {
        registered.add(s);
    }

    // 简单优先级选择示例：会员优先 -> 满减 -> 百分比 -> 无折扣
    DiscountStrategy select(Order order) {
        // 如果是会员并注册了 MemberFixedDiscount -> 优先返回第一个匹配的会员策略
        if (order.getUser() != null && order.getUser().isMember()) {
            for (DiscountStrategy s : registered) {
                if (s instanceof MemberFixedDiscount) return s;
            }
        }
        // 满减优先（按注册顺序选择第一个满足条件的满减）
        for (DiscountStrategy s : registered) {
            if (s instanceof FullReductionDiscount) {
                // 反射式读取字段不优雅，直接重新create? 为简洁，我们调用 applyDiscount 看是否改变
                double after = s.applyDiscount(order);
                if (after < order.getTotalAmount()) return s;
            }
        }
        // 百分比折扣（取第一个）
        for (DiscountStrategy s : registered) {
            if (s instanceof PercentageDiscount) return s;
        }
        // 兜底
        return new NoDiscount();
    }
}

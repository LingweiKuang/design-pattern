package com.klw.behavioral.strategy;

import com.klw.behavioral.strategy.discount.FullReductionDiscount;
import com.klw.behavioral.strategy.discount.MemberFixedDiscount;
import com.klw.behavioral.strategy.discount.PercentageDiscount;
import com.klw.behavioral.strategy.entity.Order;
import com.klw.behavioral.strategy.entity.User;

public class StrategyDemo {
    public static void main(String[] args) {
        StrategySelector selector = new StrategySelector();
        // 注册策略（启动时配置）
        selector.register(new MemberFixedDiscount(20.0));
        selector.register(new FullReductionDiscount(200.0, 30.0));
        selector.register(new PercentageDiscount(0.10)); // 10%

        OrderCalculator calc = new OrderCalculator(selector);

        User member = new User("U1", true);
        User ordinary = new User("U2", false);

        Order o1 = new Order("A100", 250.0, member);     // 会员且 >= 200
        Order o2 = new Order("A101", 250.0, ordinary);   // 非会员但满足满减
        Order o3 = new Order("A102", 80.0, ordinary);    // 小额，适用百分比或无折扣
        Order o4 = new Order("A103", 50.0, member);      // 会员小额

        System.out.println(calc.calculate(o1));
        System.out.println(calc.calculate(o2));
        System.out.println(calc.calculate(o3));
        System.out.println(calc.calculate(o4));
    }
}

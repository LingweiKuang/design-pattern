package com.klw.behavioral.state;

public class DeliveredState implements OrderState {
    public void pay(Order ctx) {
        System.out.println("订单已完成，不可重复支付");
    }

    public void ship(Order ctx) {
        System.out.println("订单已完成，不能再次发货");
    }

    public void deliver(Order ctx) {
        System.out.println("重复交付确认：已是 Delivered");
    }

    public void cancel(Order ctx) {
        throw new IllegalStateException("已交付的订单不能取消（需退货流程）");
    }

    public String name() {
        return "Delivered";
    }
}

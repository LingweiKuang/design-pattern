package com.klw.behavioral.state;

public class ShippedState implements OrderState {
    public void pay(Order ctx) {
        System.out.println("订单已发货，支付操作不可行（应已完成）");
    }

    public void ship(Order ctx) {
        System.out.println("重复发货请求：已是 Shipped");
    }

    public void deliver(Order ctx) {
        System.out.println("确认送达：Shipped -> Delivered");
        ctx.setState(new DeliveredState());
    }

    public void cancel(Order ctx) {
        throw new IllegalStateException("已发货的订单不能取消（需退货流程）");
    }

    public String name() {
        return "Shipped";
    }
}

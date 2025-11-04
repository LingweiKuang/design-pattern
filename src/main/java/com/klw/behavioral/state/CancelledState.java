package com.klw.behavioral.state;

public class CancelledState implements OrderState {
    public void pay(Order ctx) {
        throw new IllegalStateException("订单已取消，无法支付");
    }

    public void ship(Order ctx) {
        throw new IllegalStateException("订单已取消，无法发货");
    }

    public void deliver(Order ctx) {
        throw new IllegalStateException("订单已取消，无法交付");
    }

    public void cancel(Order ctx) {
        System.out.println("重复取消请求：已是 Cancelled");
    }

    public String name() {
        return "Cancelled";
    }
}

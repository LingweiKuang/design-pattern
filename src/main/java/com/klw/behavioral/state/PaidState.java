package com.klw.behavioral.state;

public class PaidState implements OrderState {
    public void pay(Order ctx) {
        System.out.println("重复支付请求：已是 Paid 状态");
    }

    public void ship(Order ctx) {
        System.out.println("发货：Paid -> Shipped");
        ctx.setState(new ShippedState());
    }

    public void deliver(Order ctx) {
        throw new IllegalStateException("Paid 状态不能直接交付，需先发货");
    }

    public void cancel(Order ctx) {
        System.out.println("已退款并取消：Paid -> Cancelled");
        ctx.setState(new CancelledState());
    }

    public String name() {
        return "Paid";
    }
}

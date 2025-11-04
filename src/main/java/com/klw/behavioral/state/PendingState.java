package com.klw.behavioral.state;

public class PendingState implements OrderState {
    public void pay(Order ctx) {
        System.out.println("支付成功：Pending -> Paid");
        ctx.setState(new PaidState());
    }

    public void ship(Order ctx) {
        throw new IllegalStateException("Pending 状态不能直接发货");
    }

    public void deliver(Order ctx) {
        throw new IllegalStateException("Pending 状态不能直接交付");
    }

    public void cancel(Order ctx) {
        System.out.println("订单取消：Pending -> Cancelled");
        ctx.setState(new CancelledState());
    }

    public String name() {
        return "Pending";
    }
}

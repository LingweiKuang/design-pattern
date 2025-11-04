package com.klw.behavioral.state;

// 环境类：Order（Context）
public class Order {
    private OrderState state;

    public Order() {
        this.state = new PendingState(); // 初始状态
    }

    void setState(OrderState s) {
        this.state = s;
    }

    public String getStatus() {
        return state.name();
    }

    // 行为委托给当前状态
    public void pay() {
        state.pay(this);
    }

    public void ship() {
        state.ship(this);
    }

    public void deliver() {
        state.deliver(this);
    }

    public void cancel() {
        state.cancel(this);
    }
}

package com.klw.behavioral.state;

// 状态接口（定义对外行为）
public interface OrderState {
    void pay(Order ctx);

    void ship(Order ctx);

    void deliver(Order ctx);

    void cancel(Order ctx);

    String name();
}

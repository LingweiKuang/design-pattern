package com.klw.behavioral.observer;

// 观察者接口
public interface Client {
    void update(String symbol, double price);
}

package com.klw.creational.abstractfactory;

// 产品接口
public interface Button {
    void render();

    void onClick(Runnable action);
}

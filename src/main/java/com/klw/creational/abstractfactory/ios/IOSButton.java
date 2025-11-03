package com.klw.creational.abstractfactory.ios;

import com.klw.creational.abstractfactory.Button;

public class IOSButton implements Button {

    public void render() {
        System.out.println("[iOS Button] 渲染圆角、毛玻璃");
    }

    public void onClick(Runnable action) {
        System.out.println("[iOS Button] 点击");
        action.run();
    }
}

package com.klw.creational.abstractfactory.material;

import com.klw.creational.abstractfactory.Button;

public class MaterialButton implements Button {
    public void render() {
        System.out.println("[Material Button] 渲染扁平阴影风格");
    }

    public void onClick(Runnable action) {
        System.out.println("[Material Button] 点击");
        action.run();
    }
}

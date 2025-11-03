package com.klw.creational.abstractfactory.material;

import com.klw.creational.abstractfactory.TextField;

public class MaterialTextField implements TextField {
    private String text = "";

    public void render() {
        System.out.println("[Material TextField] 渲染下划线");
    }

    public void setText(String t) {
        this.text = t;
    }

    public String getText() {
        return text;
    }
}

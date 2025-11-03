package com.klw.creational.abstractfactory.ios;

import com.klw.creational.abstractfactory.TextField;

public class IOSTextField implements TextField {
    private String text = "";

    public void render() {
        System.out.println("[iOS TextField] 渲染");
    }

    public void setText(String t) {
        this.text = t;
    }

    public String getText() {
        return text;
    }
}

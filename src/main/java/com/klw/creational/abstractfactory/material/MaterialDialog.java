package com.klw.creational.abstractfactory.material;

import com.klw.creational.abstractfactory.Dialog;

public class MaterialDialog implements Dialog {
    public void render() {
        System.out.println("[Material Dialog] 渲染对话框");
    }

    public void show() {
        System.out.println("[Material Dialog] 显示");
    }

    public void close() {
        System.out.println("[Material Dialog] 关闭");
    }
}

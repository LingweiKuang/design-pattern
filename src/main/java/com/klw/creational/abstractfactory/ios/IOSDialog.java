package com.klw.creational.abstractfactory.ios;

import com.klw.creational.abstractfactory.Dialog;

public class IOSDialog implements Dialog {
    public void render() {
        System.out.println("[iOS Dialog] 渲染卡片弹窗");
    }

    public void show() {
        System.out.println("[iOS Dialog] 显示");
    }

    public void close() {
        System.out.println("[iOS Dialog] 关闭");
    }
}

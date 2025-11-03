package com.klw.structural.decorator;

// --- 基础文本实现 ---
public class PlainText implements Text {
    private final String content;

    public PlainText(String content) {
        this.content = content;
    }

    @Override
    public String process() {
        return content;
    }
}

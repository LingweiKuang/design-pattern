package com.klw.structural.decorator;

// --- 具体装饰器：HTML 转义 ---
public class HtmlEscapeDecorator extends TextDecorator {
    public HtmlEscapeDecorator(Text inner) {
        super(inner);
    }

    @Override
    public String process() {
        String s = super.process();
        // 简单 HTML 转义
        s = s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
        return s;
    }
}

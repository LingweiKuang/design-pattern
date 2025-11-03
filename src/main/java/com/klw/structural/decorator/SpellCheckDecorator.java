package com.klw.structural.decorator;

// --- 拼写检查（非常简化示例） ---
public class SpellCheckDecorator extends TextDecorator {
    public SpellCheckDecorator(Text inner) {
        super(inner);
    }

    @Override
    public String process() {
        String s = super.process();
        // 极简拼写修正示例：teh -> the, recieve -> receive
        s = s.replaceAll("\\bteh\\b", "the")
                .replaceAll("\\brecieve\\b", "receive")
                .replaceAll("\\bfunciton\\b", "function");
        return s;
    }
}

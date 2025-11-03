package com.klw.structural.decorator;

// 抽象装饰器
public abstract class TextDecorator implements Text {
    private final Text inner;

    public TextDecorator(Text inner) {
        this.inner = inner;
    }

    @Override
    public String process() {
        return inner.process();
    }
}

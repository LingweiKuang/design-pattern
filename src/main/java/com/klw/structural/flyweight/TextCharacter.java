package com.klw.structural.flyweight;

// 文本字符类：包含外在状态（字符本身及样式风格）
public class TextCharacter {
    private final String character;
    private final TextStyle style;

    public TextCharacter(String character, TextStyle style) {
        this.character = character;
        this.style = style;
    }

    public void display() {
        style.applyStyle(character);  // 使用享元对象的样式
    }
}

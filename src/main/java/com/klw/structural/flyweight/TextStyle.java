package com.klw.structural.flyweight;

// 享元类：字符样式
public class TextStyle {
    private final String font;
    private final int size;
    private final String color;

    public TextStyle(String font, int size, String color) {
        this.font = font;
        this.size = size;
        this.color = color;
    }

    public void applyStyle(String text) {
        System.out.println("Applying style to: " + text + " Font: " + font + " Size: " + size + " Color: " + color);
    }
}

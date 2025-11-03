package com.klw.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

// 享元工厂：管理样式的共享
public class TextStyleFactory {
    private final Map<String, TextStyle> styles = new HashMap<>();

    public TextStyle getTextStyle(String font, int size, String color) {
        String key = font + size + color;
        if (!styles.containsKey(key)) {
            TextStyle style = new TextStyle(font, size, color);
            styles.put(key, style);
        }
        return styles.get(key);
    }

    public int getTotalStyles() {
        return styles.size();
    }
}

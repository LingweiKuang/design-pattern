package com.klw.structural.flyweight;

public class TextStyleDemo {
    public static void main(String[] args) {
        TextStyleFactory styleFactory = new TextStyleFactory();

        // 创建一些字符对象，复用样式
        TextStyle style1 = styleFactory.getTextStyle("Arial", 12, "Red");
        TextStyle style2 = styleFactory.getTextStyle("Times New Roman", 14, "Blue");
        TextStyle style3 = styleFactory.getTextStyle("Arial", 12, "Red");  // 重复使用相同的样式

        // 创建字符对象
        TextCharacter char1 = new TextCharacter("A", style1);
        TextCharacter char2 = new TextCharacter("B", style2);
        TextCharacter char3 = new TextCharacter("C", style3);

        // 显示字符，注意样式复用
        char1.display();
        char2.display();
        char3.display();

        System.out.println("Total styles used: " + styleFactory.getTotalStyles());
    }
}

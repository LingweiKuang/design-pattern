package com.klw.creational.abstractfactory;

// 抽象工厂
public interface UIFactory {
    Button createButton();

    TextField createTextField();

    Dialog createDialog();
}

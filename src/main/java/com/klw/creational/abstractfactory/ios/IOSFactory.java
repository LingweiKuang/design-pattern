package com.klw.creational.abstractfactory.ios;

import com.klw.creational.abstractfactory.Button;
import com.klw.creational.abstractfactory.Dialog;
import com.klw.creational.abstractfactory.TextField;
import com.klw.creational.abstractfactory.UIFactory;

public class IOSFactory implements UIFactory {

    private IOSFactory() {
    }

    public static class Holder {
        private static final IOSFactory INSTANCE = new IOSFactory();
    }

    public static IOSFactory getInstance() {
        return Holder.INSTANCE;
    }

    public Button createButton() {
        return new IOSButton();
    }

    public TextField createTextField() {
        return new IOSTextField();
    }

    public Dialog createDialog() {
        return new IOSDialog();
    }
}

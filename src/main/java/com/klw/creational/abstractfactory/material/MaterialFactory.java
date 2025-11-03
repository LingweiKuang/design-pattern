package com.klw.creational.abstractfactory.material;

import com.klw.creational.abstractfactory.Button;
import com.klw.creational.abstractfactory.Dialog;
import com.klw.creational.abstractfactory.TextField;
import com.klw.creational.abstractfactory.UIFactory;

public class MaterialFactory implements UIFactory {
    private MaterialFactory() {
    }

    private static class Holder {
        private static final MaterialFactory INSTANCE = new MaterialFactory();
    }

    public static MaterialFactory getInstance() {
        return Holder.INSTANCE;
    }

    public Button createButton() {
        return new MaterialButton();
    }

    public TextField createTextField() {
        return new MaterialTextField();
    }

    public Dialog createDialog() {
        return new MaterialDialog();
    }
}

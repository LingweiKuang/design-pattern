package com.klw.creational.abstractfactory;

public class UIAbstractFactoryDemo {
    public static void main(String[] args) {
        UIFactory factory = UIFactoryProvider.getCurrentFactory();
        Button materialButton = factory.createButton();
        TextField materialTextField = factory.createTextField();
        Dialog materialDialog = factory.createDialog();

        materialTextField.setText("Hello");
        materialTextField.render();
        materialButton.render();
        materialButton.onClick(() -> {
            System.out.println("Button clicked -> show dialog");
            materialDialog.show();
        });

        System.out.println("-------- 切换风格 --------");

        UIFactoryProvider.setCurrentFactory("IOS");
        factory = UIFactoryProvider.getCurrentFactory();
        Button iosButton = factory.createButton();
        TextField iosTextField = factory.createTextField();
        Dialog iosDialog = factory.createDialog();

        iosTextField.setText("Hello");
        iosTextField.render();
        iosButton.render();
        iosButton.onClick(() -> {
            System.out.println("Button clicked -> show dialog");
            iosDialog.show();
        });
    }
}

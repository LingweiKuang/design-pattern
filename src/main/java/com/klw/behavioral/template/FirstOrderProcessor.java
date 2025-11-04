package com.klw.behavioral.template;

// 首单立减活动
public class FirstOrderProcessor extends PromotionProcessor {
    private final boolean isFirstOrder;

    FirstOrderProcessor(boolean isFirstOrder) {
        this.isFirstOrder = isFirstOrder;
    }

    @Override
    protected boolean validate() {
        System.out.println("检查是否首单用户。");
        return isFirstOrder;
    }

    @Override
    protected void calculate() {
        System.out.println("首单立减20元。");
    }
}

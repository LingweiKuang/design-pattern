package com.klw.behavioral.template;

// 团购活动
public class GroupBuyProcessor extends PromotionProcessor {
    private final int groupSize;

    GroupBuyProcessor(int groupSize) {
        this.groupSize = groupSize;
    }

    @Override
    protected boolean validate() {
        System.out.println("验证团购人数是否达到门槛。");
        return groupSize >= 3;
    }

    @Override
    protected void calculate() {
        System.out.println("计算团购优惠价（例如满3人打8折）。");
    }
}

package com.klw.behavioral.template;

// 模板基类
public abstract class PromotionProcessor {
    // 模板方法：固定流程，不允许子类重写
    public final void process() {
        prepareData();
        if (!validate()) {
            System.out.println("❌ 验证未通过，活动终止。");
            return;
        }
        calculate();
        log();
        notifyUser();
    }

    // 通用步骤（所有活动相同）
    protected void prepareData() {
        System.out.println("准备活动数据...");
    }

    // 抽象步骤（留给子类实现）
    protected abstract boolean validate();

    protected abstract void calculate();

    // 公共逻辑（可选实现或钩子）
    protected void log() {
        System.out.println("记录促销日志。");
    }

    protected void notifyUser() {
        System.out.println("通知用户促销结果。");
    }
}

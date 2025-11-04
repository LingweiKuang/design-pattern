package com.klw.behavioral.template;

// 限时抢购活动
public class FlashSaleProcessor extends PromotionProcessor {
    private final int stock;
    private final int userRequested;

    FlashSaleProcessor(int stock, int userRequested) {
        this.stock = stock;
        this.userRequested = userRequested;
    }

    @Override
    protected boolean validate() {
        System.out.println("验证限时抢购条件：库存与时间窗口。");
        return stock >= userRequested;
    }

    @Override
    protected void calculate() {
        System.out.println("计算抢购价格（例如原价打7折）。");
    }
}

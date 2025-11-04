package com.klw.behavioral.template;

public class TemplateDemo {
    public static void main(String[] args) {
        PromotionProcessor flashSale = new FlashSaleProcessor(10, 3);
        PromotionProcessor groupBuy = new GroupBuyProcessor(5);
        PromotionProcessor firstOrder = new FirstOrderProcessor(true);

        System.out.println("=== 限时抢购 ===");
        flashSale.process();

        System.out.println("\n=== 团购活动 ===");
        groupBuy.process();

        System.out.println("\n=== 首单立减 ===");
        firstOrder.process();
    }
}

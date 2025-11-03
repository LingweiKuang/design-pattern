package com.klw.creational.builder;

import com.klw.creational.builder.entity.OrderItem;

import java.math.BigDecimal;

public class OrderBuilderDemo {
    public static void main(String[] args) {
        // 分阶段 builder
        Order o = OrderWithStage.builder()
                .userId("U1001")
                .addItem(new OrderItem("SKU1", 1, new BigDecimal("99.9")))
                .withOptional()
                .build();
        System.out.println(o);
        System.out.println("----------------");

        // 普通 Builder
        Order order = new Order.Builder()
                .userId("user-123")
                .addItem(new OrderItem("sku-001", 2, new BigDecimal("29.90")))
                .giftWrap(true)
                .note("请送货上门")
                .build();
        System.out.println(order);

        // 基于已有订单复制并微调
        Order modified = order.toBuilder()
                .note("改成放门口")
                .build();
        System.out.println(modified);
    }
}

package com.klw.creational.builder.entity;

import java.math.BigDecimal;

public class OrderItem {
    private final String sku;
    private final int qty;
    private final BigDecimal price;

    public OrderItem(String sku, int qty, BigDecimal price) {
        this.sku = sku;
        this.qty = qty;
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public int getQty() {
        return qty;
    }

    public BigDecimal getPrice() {
        return price;
    }
}

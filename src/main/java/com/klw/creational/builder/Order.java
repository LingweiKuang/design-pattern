package com.klw.creational.builder;


import com.klw.creational.builder.entity.OrderItem;

import java.math.BigDecimal;
import java.util.*;

public class Order {
    // 必填
    private final String orderId;
    private final String userId;
    private final List<OrderItem> items;

    // 可选
    private final boolean giftWrap;
    private final String note;
    private final Map<String, Object> ext; // 扩展字段

    private Order(Builder b) {
        this.orderId = b.orderId;
        this.userId = b.userId;
        // 防御性拷贝，保持不可变语义
        this.items = Collections.unmodifiableList(new ArrayList<>(b.items));
        this.giftWrap = b.giftWrap;
        this.note = b.note;
        this.ext = b.ext == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(b.ext));
    }

    // 对外只读访问器（略）
    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public boolean isGiftWrap() {
        return giftWrap;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return "orderId: " + orderId + ", userId: " + userId + ", items: " + items + ", giftWrap: "
                + giftWrap + ", note: " + note;
    }

    // 提供一个 toBuilder() 支持基于现有 Order 快速改造
    public Builder toBuilder() {
        return new Builder(this);
    }

    // 普通链式 Builder（允许灵活顺序，build 时做校验）
    public static class Builder {
        // 必填在逻辑上强依赖但不通过类型强制，使用 build() 校验
        private String orderId;
        private String userId;
        private List<OrderItem> items = new ArrayList<>();

        // 可选
        private boolean giftWrap = false;
        private String note;
        private Map<String, Object> ext;

        public Builder() {
        }

        // 从已有 Order 初始化 Builder （用于复制并改造）
        private Builder(Order src) {
            this.orderId = src.orderId;
            this.userId = src.userId;
            this.items = new ArrayList<>(src.items);
            this.giftWrap = src.giftWrap;
            this.note = src.note;
            this.ext = src.ext == null ? null : new HashMap<>(src.ext);
        }

        public Builder orderId(String id) {
            this.orderId = id;
            return this;
        }

        public Builder userId(String uid) {
            this.userId = uid;
            return this;
        }

        public Builder addItem(OrderItem item) {
            this.items.add(item);
            return this;
        }

        public Builder items(Collection<OrderItem> items) {
            this.items = new ArrayList<>(items);
            return this;
        }

        public Builder giftWrap(boolean flag) {
            this.giftWrap = flag;
            return this;
        }

        public Builder note(String n) {
            this.note = n;
            return this;
        }

        public Builder ext(Map<String, Object> map) {
            this.ext = new HashMap<>(map);
            return this;
        }

        // 集中校验（构建前）
        public Order build() {
            validate();
            // 生成 orderId 如果不存在
            if (this.orderId == null || this.orderId.isEmpty()) {
                this.orderId = generateOrderId();
            }
            return new Order(this);
        }

        private void validate() {
            if (userId == null || userId.isEmpty())
                throw new IllegalStateException("userId 必填");
            if (items == null || items.isEmpty())
                throw new IllegalStateException("至少需要一个订单项");
            // 例：校验金额一致性
            BigDecimal sum = items.stream()
                    .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQty())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 这里可以与 discounts/invoice 校验逻辑
            // 简化：不强制平衡金额
        }

        private static String generateOrderId() {
            return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6);
        }
    }
}

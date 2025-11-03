package com.klw.creational.builder;

import com.klw.creational.builder.entity.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderWithStage {
    public interface StepUser {
        StepItems userId(String uid);
    }

    public interface StepItems {
        StepItems addItem(OrderItem it);

        StepFinal withOptional();
    }

    public interface StepFinal {
        Order build(); /* 可选 setter */
    }

    public static StepUser builder() {
        return new Impl();
    }

    private static class Impl implements StepUser, StepItems, StepFinal {
        private String userId;
        private List<OrderItem> items = new ArrayList<>();

        // optional fields...
        public StepItems userId(String uid) {
            this.userId = uid;
            return this;
        }

        public StepItems addItem(OrderItem it) {
            this.items.add(it);
            return this;
        }

        public StepFinal withOptional() {
            return this;
        }

        public Order build() {
            if (userId == null || items.isEmpty()) throw new IllegalStateException("缺少必填");
            return new Order.Builder().userId(userId).items(items).build();
        }
    }
}

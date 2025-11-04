package com.klw.behavioral.chainofresponsibility.entity;

import lombok.Data;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicReference;

// 规则服务（线程安全，可热更新阈值）
@Data
public class RuleService {
    private final AtomicReference<Rules> current = new AtomicReference<>(new Rules(600, 50000, 2));

    @Getter
    public static class Rules {
        final int minCreditScoreForAutoApprove;
        final double autoDisburseMaxAmount;
        final int maxOverdueAllowed;

        public Rules(int minCreditScoreForAutoApprove, double autoDisburseMaxAmount, int maxOverdueAllowed) {
            this.minCreditScoreForAutoApprove = minCreditScoreForAutoApprove;
            this.autoDisburseMaxAmount = autoDisburseMaxAmount;
            this.maxOverdueAllowed = maxOverdueAllowed;
        }
    }

    public Rules get() {
        return current.get();
    }

    // 热更新
    public void update(Rules r) {
        current.set(r);
    }
}

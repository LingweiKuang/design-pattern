package com.klw.behavioral.strategy.entity;

public class CalculationResult {
    final String orderId;
    final double original;
    final double finalAmount;
    final double discount;
    final String strategyName;

    public CalculationResult(String orderId, double original, double finalAmount, double discount, String strategyName) {
        this.orderId = orderId;
        this.original = original;
        this.finalAmount = finalAmount;
        this.discount = discount;
        this.strategyName = strategyName;
    }

    public String toString() {
        return String.format("Order %s: original=%.2f, final=%.2f, discount=%.2f, strategy=%s",
                orderId, original, finalAmount, discount, strategyName);
    }
}

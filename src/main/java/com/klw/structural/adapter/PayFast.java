package com.klw.structural.adapter;

public class PayFast {
    public String payOrder(int amountInCents) throws PaymentException {
        if (amountInCents < 0) {
            throw new PaymentException("Invalid amount");
        }
        // 返回支付 ID
        return "PF-" + System.currentTimeMillis();
    }
}

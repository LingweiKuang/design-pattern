package com.klw.structural.adapter;

public class QuickPay {
    public String processPayment(double dollars) throws PaymentException {
        if (dollars < 0) {
            throw new PaymentException("Invalid dollars");
        }
        // 返回支付 ID
        return "QP-" + System.currentTimeMillis();
    }
}

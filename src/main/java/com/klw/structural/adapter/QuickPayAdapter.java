package com.klw.structural.adapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuickPayAdapter implements PaymentGateway {
    private final QuickPay quickPay;

    public QuickPayAdapter(QuickPay quickPay) {
        this.quickPay = quickPay;
    }

    @Override
    public String charge(int amountInCents) throws PaymentException {
        // 转换为美元, 利用 quickPay 支付
        double dollars = centsToDollars(amountInCents);
        return quickPay.processPayment(dollars);
    }

    private double centsToDollars(int cents) {
        return cents / 100.0;
    }
}

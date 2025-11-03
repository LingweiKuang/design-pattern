package com.klw.structural.adapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PayFastAdapter implements PaymentGateway {
    private final PayFast payFast;

    public PayFastAdapter(PayFast payFast) {
        this.payFast = payFast;
    }

    @Override
    public String charge(int amountInCents) throws PaymentException {
        return payFast.payOrder(amountInCents);
    }
}

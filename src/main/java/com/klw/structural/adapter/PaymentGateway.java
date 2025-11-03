package com.klw.structural.adapter;

public interface PaymentGateway {
    // PaymentGateway.java - 统一接口，金额以分为单位 (int cents)
    String charge(int amountInCents) throws PaymentException;
}

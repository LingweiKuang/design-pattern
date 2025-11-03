package com.klw.creational.factory.entity;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentRequest {
    public String orderId;
    public BigDecimal amount;
    public Map<String, Object> ext;

    public PaymentRequest(String orderId, BigDecimal amount, Map<String, Object> ext) {
        this.orderId = orderId;
        this.amount = amount;
        this.ext = ext;
    }
}

package com.klw.creational.factory;

import com.klw.creational.factory.entity.PayChannelConfig;
import com.klw.creational.factory.entity.PaymentRequest;
import com.klw.creational.factory.entity.PaymentResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MockPayHandler implements PayHandler {
    private AtomicInteger idx = new AtomicInteger(0);

    @Override
    public void initialize(PayChannelConfig config) {
    }

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("mockId", "MOCK-" + idx.incrementAndGet());
        return PaymentResponse.ok(data);
    }

    @Override
    public PaymentResponse handleCallback(Map<String, String> params) {
        return PaymentResponse.ok(Collections.singletonMap("callback", "ok"));
    }
}

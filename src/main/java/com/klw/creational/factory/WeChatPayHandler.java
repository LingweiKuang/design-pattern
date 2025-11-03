package com.klw.creational.factory;

import com.klw.creational.factory.entity.PayChannelConfig;
import com.klw.creational.factory.entity.PaymentException;
import com.klw.creational.factory.entity.PaymentRequest;
import com.klw.creational.factory.entity.PaymentResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WeChatPayHandler implements PayHandler {
    private volatile boolean initialized = false;
    private String mchId;
    private String certPath;
    // thread-safe http client, etc.

    @Override
    public void initialize(PayChannelConfig config) throws PaymentException {
        if (initialized) return; // idempotent
        // load props
        this.mchId = config.props.get("mchId");
        this.certPath = config.props.get("certPath");
        // load cert, create http client...
        initialized = true;
    }

    @Override
    public PaymentResponse pay(PaymentRequest request) throws PaymentException {
        if (!initialized) throw new PaymentException(PaymentException.Code.INIT_FAILED, "not initialized");
        // build request, sign, send http, parse response
        Map<String, Object> data = new HashMap<>();
        data.put("payUrl", "https://wxpay.example/pay?order=" + request.orderId);
        System.out.println("[Wechat]: pay: " + request);
        return PaymentResponse.ok(data);
    }

    @Override
    public PaymentResponse handleCallback(Map<String, String> params) throws PaymentException {
        // verify signature, update order status...
        return PaymentResponse.ok(Collections.singletonMap("status", "SUCCESS"));
    }
}

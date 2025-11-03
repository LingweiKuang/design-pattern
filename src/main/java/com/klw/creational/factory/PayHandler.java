package com.klw.creational.factory;

import com.klw.creational.factory.entity.PayChannelConfig;
import com.klw.creational.factory.entity.PaymentException;
import com.klw.creational.factory.entity.PaymentRequest;
import com.klw.creational.factory.entity.PaymentResponse;

import java.util.Map;

public interface PayHandler {
    void initialize(PayChannelConfig config) throws PaymentException;

    PaymentResponse pay(PaymentRequest request) throws PaymentException;

    PaymentResponse handleCallback(Map<String, String> params) throws PaymentException;

    default PaymentResponse refund(Object req) throws PaymentException {
        throw new UnsupportedOperationException();
    }

    default PaymentResponse query(Object req) throws PaymentException {
        throw new UnsupportedOperationException();
    }
}

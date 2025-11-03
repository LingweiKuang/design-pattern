package com.klw.creational.factory.entity;

import java.util.Map;

public class PaymentResponse {
    public boolean success;
    public String code;
    public String message;
    public Map<String, Object> data;

    public static PaymentResponse ok(Map<String, Object> data) {
        PaymentResponse r = new PaymentResponse();
        r.success = true;
        r.data = data;
        return r;
    }

    public static PaymentResponse fail(String code, String msg) {
        PaymentResponse r = new PaymentResponse();
        r.success = false;
        r.code = code;
        r.message = msg;
        return r;
    }
}

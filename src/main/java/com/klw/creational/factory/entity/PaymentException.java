package com.klw.creational.factory.entity;

public class PaymentException extends RuntimeException {
    public enum Code {CHANNEL_NOT_FOUND, INIT_FAILED, NETWORK_ERROR, BUSINESS_ERROR, INVALID_PARAM}

    private final Code code;

    public PaymentException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}

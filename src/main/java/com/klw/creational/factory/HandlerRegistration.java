package com.klw.creational.factory;

import java.util.function.Supplier;

public class HandlerRegistration {
    private final Supplier<PayHandler> supplier;
    private final boolean singleton;

    public HandlerRegistration(Supplier<PayHandler> supplier, boolean singleton) {
        this.supplier = supplier;
        this.singleton = singleton;
    }

    public Supplier<PayHandler> getSupplier() {
        return supplier;
    }

    public boolean isSingleton() {
        return singleton;
    }
}

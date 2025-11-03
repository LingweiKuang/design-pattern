package com.klw.creational.factory;

import com.klw.creational.factory.entity.PayChannelConfig;
import com.klw.creational.factory.entity.PaymentException;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

// 支付服务工厂
public class PayHandlerFactory {
    private static final PayHandlerFactory INSTANCE = new PayHandlerFactory();

    public static PayHandlerFactory getInstance() {
        return INSTANCE;
    }

    private final ConcurrentHashMap<String, HandlerRegistration> registry = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, PayHandler> singletonCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, PayChannelConfig> configs = new ConcurrentHashMap<>();

    private PayHandlerFactory() {
    }

    public void register(String type, Supplier<PayHandler> supplier, boolean singleton) {
        registry.put(type, new HandlerRegistration(supplier, singleton));
    }

    public void unregister(String type) {
        registry.remove(type);
        singletonCache.remove(type);
        configs.remove(type);
    }

    public void putConfig(String type, PayChannelConfig cfg) {
        configs.put(type, cfg);
    }

    public PayHandler getHandler(String type) throws PaymentException {
        HandlerRegistration reg = registry.get(type);
        if (reg == null)
            throw new PaymentException(PaymentException.Code.CHANNEL_NOT_FOUND, "channel " + type + " not registered");
        if (reg.isSingleton()) {
            // safe init and cache
            return singletonCache.computeIfAbsent(type, t -> {
                PayHandler h = reg.getSupplier().get();
                PayChannelConfig cfg = configs.get(t);
                try {
                    if (cfg != null) h.initialize(cfg);
                } catch (PaymentException e) {
                    throw new RuntimeException(e); // convert to unchecked inside computeIfAbsent
                }
                return h;
            });
        } else {
            PayHandler handler = reg.getSupplier().get();
            PayChannelConfig cfg = configs.get(type);
            if (cfg != null) handler.initialize(cfg);
            return handler;
        }
    }

    public Set<String> listRegistered() {
        return registry.keySet();
    }
}

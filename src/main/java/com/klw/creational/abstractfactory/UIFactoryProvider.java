package com.klw.creational.abstractfactory;

import com.klw.creational.abstractfactory.ios.IOSFactory;
import com.klw.creational.abstractfactory.material.MaterialFactory;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

// UIFactoryProvider: 运行时注册 / 切换
public class UIFactoryProvider {
    private static final ConcurrentMap<String, UIFactory> registry = new ConcurrentHashMap<>();
    private static final AtomicReference<UIFactory> current = new AtomicReference<>();

    static {
        // 默认注册两个
        registry.put("IOS", IOSFactory.getInstance());
        registry.put("MATERIAL", MaterialFactory.getInstance());
        // 默认主题
        current.set(registry.get("MATERIAL"));
    }

    public static void register(String name, UIFactory factory) {
        registry.put(name.toUpperCase(Locale.ROOT), factory);
    }

    public static void unregister(String name) {
        registry.remove(name.toUpperCase(Locale.ROOT));
    }

    public static UIFactory getFactory(String name) {
        return registry.get(name.toUpperCase(Locale.ROOT));
    }

    public static UIFactory getCurrentFactory() {
        return current.get();
    }

    // 运行时切换当前主题
    public static void setCurrentFactory(String name) {
        UIFactory f = getFactory(name);
        if (f == null) throw new IllegalArgumentException("未注册主题: " + name);
        current.set(f);
    }

    public static Set<String> listRegistered() {
        return registry.keySet();
    }
}

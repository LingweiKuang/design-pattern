package com.klw.creational.singleton;

import java.util.concurrent.atomic.AtomicInteger;

// 默认实现：懒加载 + 线程安全（静态内部类 Holder），使用 AtomicInteger 保证并发下序号不冲突
public class DefaultOrderIdGenerator implements OrderIdGenerator {
    private static final int SEQ_MAX = 99999;
    private final AtomicInteger seq = new AtomicInteger(0);

    private DefaultOrderIdGenerator() {
    }

    public static class Holder {
        private static final DefaultOrderIdGenerator INSTANCE = new DefaultOrderIdGenerator();
    }

    public static DefaultOrderIdGenerator getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String generate() {
        long now = System.currentTimeMillis();
        int s = seq.updateAndGet(i -> (i + 1) % (SEQ_MAX + 1));
        // 例如：1633024800000-00001 -> 使用简单连接或自定义格式
        return now + String.format("%05d", s);
    }
}

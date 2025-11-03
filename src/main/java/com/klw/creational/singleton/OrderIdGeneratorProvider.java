package com.klw.creational.singleton;

// Holder/Provider：所有业务代码从这里获取单例，便于测试替换
public class OrderIdGeneratorProvider {

    private static volatile OrderIdGenerator INSTANCE = DefaultOrderIdGenerator.getInstance();

    private OrderIdGeneratorProvider() {
    }

    public static OrderIdGenerator get() {
        return INSTANCE;
    }

    /**
     * 允许在测试时替换实现（注意：线上慎用）。
     * 推荐在测试结束后恢复原始实现，或在测试框架中做隔离。
     */
    public static void setForTest(OrderIdGenerator custom) {
        INSTANCE = custom;
    }

    // 恢复默认实现（测试用）
    public static void resetToDefault() {
        INSTANCE = DefaultOrderIdGenerator.getInstance();
    }

}

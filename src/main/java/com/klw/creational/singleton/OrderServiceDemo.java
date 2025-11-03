package com.klw.creational.singleton;

public class OrderServiceDemo {
    public static void main(String[] args) {
        String orderId = OrderIdGeneratorProvider.get().generate();
        // 业务逻辑...
        System.out.println(orderId);
    }
}

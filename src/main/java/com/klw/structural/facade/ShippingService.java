package com.klw.structural.facade;

// 子系统 4：发货安排
public class ShippingService {
    public void scheduleShipping(int orderId) {
        // 安排发货
        System.out.println("Scheduling shipping for order: " + orderId);
    }
}

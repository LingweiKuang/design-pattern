package com.klw.structural.facade;

// 子系统 5：用户通知
public class NotificationService {
    public void sendOrderConfirmation(int orderId) {
        // 发送订单确认通知
        System.out.println("Sending order confirmation for order: " + orderId);
    }
}

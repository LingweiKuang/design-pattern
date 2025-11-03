package com.klw.structural.facade;

// 子系统 3：支付处理
public class PaymentService {
    public boolean processPayment(double amount) {
        // 进行支付
        System.out.println("Processing payment of: " + amount);
        return true;  // 假设支付成功
    }
}

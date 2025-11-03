package com.klw.structural.adapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderService {
    private final PaymentGateway paymentGateway;

    public OrderService(PaymentGateway paymentGateway) {
        // 注入支付方式
        this.paymentGateway = paymentGateway;
    }

    public void placeOrder(String orderId, int amountInCents) throws PaymentException {
        try {
            String txId = paymentGateway.charge(amountInCents);
            log.info("order {} Charging  with id {}", orderId, txId);
        } catch (PaymentException e) {
            log.error("order {} charging failed", orderId, e);
        }
    }

    public static void main(String[] args) {
        // 使用 PayFast
        PaymentGateway pfGateway = new PayFastAdapter(new PayFast());
        OrderService svc1 = new OrderService(pfGateway);
        svc1.placeOrder("order-1001", 1999); // ￥19.99

        // 切换到 QuickPay（单位适配仍然由适配器完成）
        PaymentGateway qpGateway = new QuickPayAdapter(new QuickPay());
        OrderService svc2 = new OrderService(qpGateway);
        svc2.placeOrder("order-1002", 2500); // ￥25.00
    }
}

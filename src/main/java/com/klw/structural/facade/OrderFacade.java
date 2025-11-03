package com.klw.structural.facade;

// 外观类：订单下单外观
public class OrderFacade {
    private final InventoryService inventoryService;
    private final PricingService pricingService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final NotificationService notificationService;

    public OrderFacade() {
        inventoryService = new InventoryService();
        pricingService = new PricingService();
        paymentService = new PaymentService();
        shippingService = new ShippingService();
        notificationService = new NotificationService();
    }

    public void placeOrder(int productId, int quantity) {
        // 1. 检查库存
        if (!inventoryService.checkStock(productId, quantity)) {
            System.out.println("Stock not available for product: " + productId);
            return;
        }

        // 2. 计算价格
        double price = pricingService.calculatePrice(productId, quantity);

        // 3. 处理支付
        if (!paymentService.processPayment(price)) {
            System.out.println("Payment failed for order.");
            return;
        }

        // 4. 安排发货
        shippingService.scheduleShipping(productId);

        // 5. 发送通知
        notificationService.sendOrderConfirmation(productId);

        System.out.println("Order placed successfully!");
    }
}

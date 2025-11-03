package com.klw.structural.facade;

// 子系统 2：价格计算
public class PricingService {
    public double calculatePrice(int productId, int quantity) {
        // 进行价格计算
        System.out.println("Calculating price for product: " + productId);
        return 100.0 * quantity;  // 假设每个商品 100 单位货币
    }
}

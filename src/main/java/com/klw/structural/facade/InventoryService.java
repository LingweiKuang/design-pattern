package com.klw.structural.facade;

// 子系统 1：库存检查
public class InventoryService {
    public boolean checkStock(int productId, int quantity) {
        // 进行库存检查
        System.out.println("Checking stock for product: " + productId);
        return true;  // 假设库存充足
    }
}

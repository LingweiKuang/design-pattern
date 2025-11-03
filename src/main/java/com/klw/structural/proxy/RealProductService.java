package com.klw.structural.proxy;

// 真实主题：远程商品服务
public class RealProductService implements ProductService {

    @Override
    public String getProductDetails(int productId) {
        // 模拟远程服务的耗时操作
        System.out.println("Fetching product details from remote server for product: " + productId);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("error: " + e.getMessage());
        }
        return "Product Details for ID " + productId;
    }
}

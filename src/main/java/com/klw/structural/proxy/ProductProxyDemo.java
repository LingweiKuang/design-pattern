package com.klw.structural.proxy;

public class ProductProxyDemo {
    public static void main(String[] args) {
        ProductService productService = new ProductServiceProxy();

        // 第一次调用，真实服务会被调用并返回数据
        System.out.println(productService.getProductDetails(1));

        // 第二次调用，应该从缓存中获取数据，而不是远程调用
        System.out.println(productService.getProductDetails(1));

        // 请求其他商品时，缓存无效，真实服务重新被调用
        System.out.println(productService.getProductDetails(2));
    }
}

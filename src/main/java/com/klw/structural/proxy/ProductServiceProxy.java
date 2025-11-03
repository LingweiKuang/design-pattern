package com.klw.structural.proxy;

public class ProductServiceProxy implements ProductService {
    private RealProductService realProductService;
    private String cachedProductDetails = null;
    private int cachedProductId = -1;

    @Override
    public String getProductDetails(int productId) {
        // 如果缓存中有数据，直接返回
        if (productId == cachedProductId && cachedProductDetails != null) {
            System.out.println("Returning cached data for product: " + productId);
            return cachedProductDetails;
        }

        // 如果缓存没有数据，调用真实服务
        if (realProductService == null) {
            realProductService = new RealProductService();
        }

        String productDetails = realProductService.getProductDetails(productId);

        // 缓存结果
        cachedProductId = productId;
        cachedProductDetails = productDetails;

        return productDetails;
    }
}

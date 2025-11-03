package com.klw.structural.facade;

public class OrderFacadeDemo {
    public static void main(String[] args) {
        // 客户端只需要调用外观类的单一方法
        OrderFacade orderFacade = new OrderFacade();
        orderFacade.placeOrder(101, 2);  // 下单：商品ID 101，数量 2
    }
}

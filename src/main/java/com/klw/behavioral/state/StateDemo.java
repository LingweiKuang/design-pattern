package com.klw.behavioral.state;

public class StateDemo {
    public static void main(String[] args) {
        Order o = new Order();
        System.out.println("初始状态: " + o.getStatus()); // Pending

        o.pay();   // Pending -> Paid
        System.out.println("当前状态: " + o.getStatus());

        o.ship();  // Paid -> Shipped
        System.out.println("当前状态: " + o.getStatus());

        try {
            // 发货后尝试取消（应抛异常）
            o.cancel();
        } catch (IllegalStateException e) {
            System.out.println("取消失败: " + e.getMessage());
        }

        o.deliver(); // Shipped -> Delivered
        System.out.println("最终状态: " + o.getStatus());

        // 再尝试 pay（不合法，但实现中只是打印）
        o.pay();
        System.out.println("结束状态: " + o.getStatus());
    }
}

package com.klw.structural.bridge;

import java.util.HashMap;
import java.util.Map;

public class OrderNotification extends Notification {

    // 订单服务需要通知的具体内容 - 私有属性

    public OrderNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    protected String buildTitle() {
        return "Order";
    }

    @Override
    protected String buildBody() {
        return "";
    }

    protected Map<String, Object> buildMeta() {
        return new HashMap<>();
    }
}

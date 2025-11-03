package com.klw.structural.bridge;

import java.util.HashMap;
import java.util.Map;

public class AlertNotification extends Notification {

    // 告警服务需要通知的具体内容 - 私有属性

    public AlertNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    protected String buildTitle() {
        return "Alert";
    }

    @Override
    protected String buildBody() {
        return "";
    }

    protected Map<String, Object> buildMeta() {
        return new HashMap<>();
    }
}

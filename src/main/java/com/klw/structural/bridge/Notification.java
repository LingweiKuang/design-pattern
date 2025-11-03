package com.klw.structural.bridge;

import java.util.HashMap;
import java.util.Map;

public abstract class Notification {
    // bridge: 抽象持有实现者引用
    protected final MessageSender sender;

    protected Notification(MessageSender sender) {
        this.sender = sender;
    }

    // 由各种服务的通知子类构造具体的通知内容
    protected abstract String buildTitle();

    protected abstract String buildBody();

    protected Map<String, Object> buildMeta() {
        return new HashMap<>();
    }

    /**
     * 调用具体的 sender 通知至具体客户
     */
    public boolean notifyUser() {
        String title = buildTitle();
        String body = buildBody();
        Map<String, Object> meta = buildMeta();
        return sender.send(title, body, meta);
    }
}

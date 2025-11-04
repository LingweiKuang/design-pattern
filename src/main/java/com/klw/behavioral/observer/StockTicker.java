package com.klw.behavioral.observer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

// 主题（可观察者）
public class StockTicker {
    // 使用并发集合以便简单的线程安全（读多写少场景友好）
    // key = symbol, value = set of clients subscribed to that symbol
    private final Map<String, CopyOnWriteArraySet<Client>> subscribers = new ConcurrentHashMap<>();

    // 订阅特定 symbol；若 symbol 为 "*" 则表示订阅全部
    public void subscribe(Client c, String symbol) {
        if (c == null || symbol == null) throw new IllegalArgumentException("client & symbol required");
        subscribers.computeIfAbsent(symbol, k -> new CopyOnWriteArraySet<>()).add(c);
    }

    // 退订特定 symbol
    public void unsubscribe(Client c, String symbol) {
        if (c == null || symbol == null) throw new IllegalArgumentException("client & symbol required");
        Set<Client> set = subscribers.get(symbol);
        if (set != null) {
            set.remove(c);
            if (set.isEmpty()) {
                subscribers.remove(symbol, set);
            }
        }
    }

    // 更新价格并通知订阅者（先通知特定 symbol 的订阅者，再通知订阅全部的客户端）
    public void updatePrice(String symbol, double price) {
        if (symbol == null) throw new IllegalArgumentException("symbol required");
        // notify subscribers of this symbol
        Set<Client> specific = subscribers.get(symbol);
        if (specific != null) {
            for (Client c : specific) {
                safeNotify(c, symbol, price);
            }
        }
        // notify subscribers of all symbols (using "*" as wildcard)
        Set<Client> all = subscribers.get("*");
        if (all != null) {
            for (Client c : all) {
                safeNotify(c, symbol, price);
            }
        }
    }

    private void safeNotify(Client c, String symbol, double price) {
        try {
            c.update(symbol, price);
        } catch (Throwable t) {
            // 保护主题：单个观察者异常不应影响其他观察者
            System.err.println("通知失败给客户端: " + c + "，错误: " + t.getMessage());
        }
    }
}

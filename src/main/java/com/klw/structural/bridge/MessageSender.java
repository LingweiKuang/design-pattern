package com.klw.structural.bridge;

import java.util.Map;

public interface MessageSender {
    boolean send(String title, String body, Map<String, Object> meta);
}

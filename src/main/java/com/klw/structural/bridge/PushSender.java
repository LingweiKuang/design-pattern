package com.klw.structural.bridge;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class PushSender implements MessageSender {

    @Override
    public boolean send(String title, String body, Map<String, Object> meta) {
        // 具体发送服务
        log.info("[Push] Title: [{}] Body: [{}] meta: [{}]", title, body, meta);
        return true;
    }
}

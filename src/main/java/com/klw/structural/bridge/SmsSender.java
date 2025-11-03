package com.klw.structural.bridge;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class SmsSender implements MessageSender {

    // 短信通知所需各种服务，例如 smsServer、targetNumber、短信模板 等

    @Override
    public boolean send(String title, String body, Map<String, Object> meta) {
        // 具体发送服务
        log.info("[SMS] Title: [{}] Body: [{}] meta: [{}]", title, body, meta);
        return true;
    }
}

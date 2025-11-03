package com.klw.structural.bridge;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class EmailSender implements MessageSender {

    // 邮箱通知所需各种服务，例如 emailServer、targetEmail 等

    @Override
    public boolean send(String title, String body, Map<String, Object> meta) {
        // 具体发送服务
        log.info("[Email] Title: [{}] Body: [{}] meta: [{}]", title, body, meta);
        return true;
    }
}

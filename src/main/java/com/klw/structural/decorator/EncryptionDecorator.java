package com.klw.structural.decorator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

// --- 加密（示例使用 Base64 模拟“可逆加密”） ---
public class EncryptionDecorator extends TextDecorator {
    public EncryptionDecorator(Text inner) {
        super(inner);
    }

    @Override
    public String process() {
        String plain = super.process();
        // 生产级请使用标准加密（AES/GCM 等），并管理密钥
        return Base64.getEncoder().encodeToString(plain.getBytes(StandardCharsets.UTF_8));
    }

    // 解密方法
    public static String decrypt(String base64) {
        byte[] decoded = Base64.getDecoder().decode(base64);
        return new String(decoded, StandardCharsets.UTF_8);
    }
}

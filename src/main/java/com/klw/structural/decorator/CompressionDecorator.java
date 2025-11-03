package com.klw.structural.decorator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

// --- 压缩（使用 Deflater + Base64 编码输出） ---
public class CompressionDecorator extends TextDecorator {

    public CompressionDecorator(Text inner) {
        super(inner);
    }

    @Override
    public String process() {
        try {
            byte[] input = super.process().getBytes(StandardCharsets.UTF_8);
            Deflater deflater = new Deflater();
            deflater.setInput(input);
            deflater.finish();
            byte[] buffer = new byte[1024];
            int written;
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            while (!deflater.finished()) {
                written = deflater.deflate(buffer);
                baos.write(buffer, 0, written);
            }
            deflater.end();
            byte[] compressed = baos.toByteArray();
            return Base64.getEncoder().encodeToString(compressed);
        } catch (Exception e) {
            throw new RuntimeException("Compression failed", e);
        }
    }

    // 解压缩
    public static String decompress(String base64) {
        try {
            byte[] compressed = Base64.getDecoder().decode(base64);
            Inflater inflater = new Inflater();
            inflater.setInput(compressed);
            byte[] buffer = new byte[1024];
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                if (count == 0) break;
                baos.write(buffer, 0, count);
            }
            inflater.end();
            return baos.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException("Decompression failed", e);
        }
    }
}

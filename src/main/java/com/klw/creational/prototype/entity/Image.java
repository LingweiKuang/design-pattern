package com.klw.creational.prototype.entity;

import lombok.Data;

@Data
public class Image {
    private String url;
    private String checksum;

    public Image(String url, String checksum) {
        this.url = url;
        this.checksum = checksum;
    }

    public Image copy() {
        // 图片资源通常只复制引用/metadata，实际二进制不重拷贝
        return new Image(this.url, this.checksum);
    }
}

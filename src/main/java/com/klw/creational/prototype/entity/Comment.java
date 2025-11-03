package com.klw.creational.prototype.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private String user;
    private String text;
    private Date createdAt;

    public Comment(String u, String t, Date d) {
        this.user = u;
        this.text = t;
        this.createdAt = d == null ? null : (Date) d.clone();
    }
}

package com.klw.behavioral.strategy.entity;

import lombok.Getter;

// 订单与用户简化模型
@Getter
public class User {
    final String id;
    final boolean isMember;

    public User(String id, boolean isMember) {
        this.id = id;
        this.isMember = isMember;
    }
}

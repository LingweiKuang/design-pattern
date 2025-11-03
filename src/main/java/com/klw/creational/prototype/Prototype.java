package com.klw.creational.prototype;

import com.klw.creational.prototype.entity.CopyContext;

public interface Prototype<T> {
    /**
     * 根据上下文拷贝自身，返回新实例。
     * 实现者负责处理 ID 重置与字段拷贝策略。
     */
    T copy(CopyContext ctx);
}

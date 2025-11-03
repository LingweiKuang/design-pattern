package com.klw.creational.prototype.entity;

import java.util.HashMap;
import java.util.Map;

public class CopyContext {
    // 控制每个字段的拷贝深度；key 可以是字段名或路径
    private final Map<String, CopyDepth> fieldDepth = new HashMap<>();

    public CopyContext shallow(String field) {
        fieldDepth.put(field, CopyDepth.SHALLOW);
        return this;
    }

    public CopyContext deep(String field) {
        fieldDepth.put(field, CopyDepth.DEEP);
        return this;
    }

    public CopyDepth getDepth(String field) {
        return fieldDepth.getOrDefault(field, CopyDepth.DEEP);
    }
}

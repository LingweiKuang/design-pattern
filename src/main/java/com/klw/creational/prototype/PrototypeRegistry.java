package com.klw.creational.prototype;

import com.klw.creational.prototype.entity.CopyContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class PrototypeRegistry {
    private final Map<String, Product> templates = new ConcurrentHashMap<>();

    public void registerTemplate(String key, Product p) {
        templates.put(key, p);
    }

    public void unregister(String key) {
        templates.remove(key);
    }

    /**
     * 返回基于 templateKey 克隆出来的产品。
     * caller 可传 ctx 指定浅/深策略，并传入 modifications（比如新价格、新库存等）。
     */
    public Product cloneFrom(String templateKey, CopyContext ctx, Consumer<Product> modifications) {
        Product template = templates.get(templateKey);
        if (template == null) throw new IllegalArgumentException("模板不存在: " + templateKey);
        Product copy = template.copy(ctx == null ? new CopyContext() : ctx);
        // 业务层常常需要：重置/生成新的 SKU/ID 并应用快速修改
        copy.setId(null);
        if (modifications != null) modifications.accept(copy);
        return copy;
    }
}

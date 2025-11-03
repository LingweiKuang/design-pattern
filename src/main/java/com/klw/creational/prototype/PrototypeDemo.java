package com.klw.creational.prototype;

import com.klw.creational.prototype.entity.CopyContext;
import com.klw.creational.prototype.entity.Image;
import com.klw.creational.prototype.entity.Inventory;

import javax.management.Attribute;
import java.math.BigDecimal;
import java.util.List;

public class PrototypeDemo {
    public static void main(String[] args) {
        // 启动或后台：注册模板
        PrototypeRegistry registry = new PrototypeRegistry();
        Product existing = new Product();
        existing.setId(1L);
        existing.setName("Name");
        existing.setPrice(BigDecimal.TEN);
        existing.setImages(List.of(new Image("url", "checksum")));
        existing.setAttributes(List.of(new Attribute("key", "value")));
        existing.setInventory(new Inventory(1, "warehouse"));
        registry.registerTemplate("TEMPLATE-123", existing);
        System.out.println(existing);

        // 用户点击“复制商品”, 从原型中拷贝一份进行部分属性修改
        CopyContext ctx = new CopyContext()
                .shallow("images")      // 图片共享引用（节省性能）
                .deep("attributes");    // 属性复制
        Product newProduct = registry.cloneFrom("TEMPLATE-123", ctx, p -> {
            p.setPrice(p.getPrice().multiply(new BigDecimal("0.9"))); // 调整价格
            p.getInventory().setStock(0); // 新商品库存置 0
            p.setName(p.getName() + " - Copy");
        });
        System.out.println(newProduct);
    }
}

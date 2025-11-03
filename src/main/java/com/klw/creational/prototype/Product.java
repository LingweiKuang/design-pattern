package com.klw.creational.prototype;

import com.klw.creational.prototype.entity.*;
import lombok.Data;

import javax.management.Attribute;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Product implements Prototype<Product> {

    private static final AtomicLong ID_SEQ = new AtomicLong(1000);

    private Long id; // 持久化主键，应在复制时重置为 null 或新值
    private String name;
    private BigDecimal price;
    private List<Image> images;         // 资源类：通常只需要浅拷贝（共享同一资源引用）
    private List<Attribute> attributes; // 值对象：通常深拷贝或按需复制
    private List<Comment> comments;     // 可变、需深拷贝以保存编辑历史
    private Inventory inventory;        // 库存记录（独立副本）

    public Product() {
    }

    @Override
    public Product copy(CopyContext ctx) {
        Product p = new Product();
        // id 重置为 null 或新生成的业务临时 id（持久化时再生成主键）
        p.id = null;
        p.name = this.name;
        p.price = this.price;

        // images 按策略：默认为 SHALLOW（共享资源）
        if (ctx.getDepth("images") == CopyDepth.SHALLOW) {
            p.images = this.images == null ? null : new ArrayList<>(this.images); // list shallow copy elements share ref
        } else {
            p.images = this.images == null ? null : deepCopyImages(this.images);
        }

        // attributes 默认为 DEEP（复制属性对象）
        if (ctx.getDepth("attributes") == CopyDepth.SHALLOW) {
            p.attributes = this.attributes == null ? null : new ArrayList<>(this.attributes);
        } else {
            p.attributes = this.attributes == null ? null : deepCopyAttributes(this.attributes);
        }

        // comments 通常需要深拷贝
        p.comments = this.comments == null ? new ArrayList<>() : deepCopyComments(this.comments);

        // inventory 深拷贝（独立库存记录）
        p.inventory = this.inventory == null ? null : this.inventory.copy();

        return p;
    }

    private List<Image> deepCopyImages(List<Image> src) {
        List<Image> out = new ArrayList<>();
        for (Image img : src) out.add(img.copy()); // image.copy() 做实际克隆或复制元数据
        return out;
    }

    private List<Attribute> deepCopyAttributes(List<Attribute> src) {
        List<Attribute> out = new ArrayList<>();
        for (Attribute a : src) out.add(new Attribute(a.getName(), a.getValue()));
        return out;
    }

    private List<Comment> deepCopyComments(List<Comment> src) {
        List<Comment> out = new ArrayList<>();
        for (Comment c : src) out.add(new Comment(c.getUser(), c.getText(), c.getCreatedAt()));
        return out;
    }
}

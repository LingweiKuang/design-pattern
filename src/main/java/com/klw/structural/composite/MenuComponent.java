package com.klw.structural.composite;

import java.util.List;

public abstract class MenuComponent {
    protected final String name;

    public MenuComponent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 默认抛出，叶子不支持 add/remove
    public void add(MenuComponent comp) {
        throw new UnsupportedOperationException();
    }

    public void remove(MenuComponent comp) {
        throw new UnsupportedOperationException();
    }

    public List<MenuComponent> getChildren() {
        throw new UnsupportedOperationException();
    }

    /**
     * 按角色过滤并返回一个新的节点（副本）或 null（表示对该角色不可见）
     * 设计选择：返回新对象，原树不变（更易于 reasoning / 并发安全）。
     */
    public abstract MenuComponent filterByRole(String role);

    /**
     * 渲染为可读文本（用于演示），indent 表示缩进层级
     */
    public abstract void render(String indent);
}

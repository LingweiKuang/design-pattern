package com.klw.structural.composite;

import java.util.*;

public class MenuGroup extends MenuComponent {
    private final List<MenuComponent> children = new ArrayList<>();
    // 组是否有自己的权限（可选），若为空则代表继承孩子权限（常见）
    private final Set<String> allowedRoles;

    public MenuGroup(String name) {
        this(name, Collections.emptySet());
    }

    public MenuGroup(String name, Set<String> allowedRoles) {
        super(name);
        this.allowedRoles = (allowedRoles == null) ? Collections.emptySet() : new HashSet<>(allowedRoles);
    }

    @Override
    public void add(MenuComponent comp) {
        children.add(comp);
    }

    @Override
    public void remove(MenuComponent comp) {
        children.remove(comp);
    }

    @Override
    public List<MenuComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    private boolean groupAllows(String role) {
        return allowedRoles.isEmpty() || allowedRoles.contains(role);
    }

    @Override
    public MenuComponent filterByRole(String role) {
        // 如果组本身被限制且该角色不满足，则整个组不可见
        if (!groupAllows(role)) return null;

        MenuGroup copy = new MenuGroup(this.name, this.allowedRoles);
        for (MenuComponent child : children) {
            MenuComponent filteredChild = child.filterByRole(role);
            if (filteredChild != null) {
                copy.add(filteredChild);
            }
        }
        // 如果组没有任何子（叶子都被过滤掉），应决定是否显示空目录——这里选择不显示（返回 null）
        if (copy.children.isEmpty()) return null;
        return copy;
    }

    @Override
    public void render(String indent) {
        System.out.println(indent + "+ " + name);
        for (MenuComponent child : children) {
            child.render(indent + "  ");
        }
    }
}

package com.klw.structural.composite;

import java.util.HashSet;
import java.util.Set;

public class MenuItem extends MenuComponent {
    private final Set<String> allowedRoles;
    private final String path;

    public MenuItem(String name, String path, Set<String> allowedRoles) {
        super(name);
        this.path = path;
        this.allowedRoles = new HashSet<>(allowedRoles);
    }

    public boolean isVisibleFor(String role) {
        // 可以细化为角色继承/权限表达式
        return allowedRoles.contains(role);
    }

    @Override
    public MenuComponent filterByRole(String role) {
        return isVisibleFor(role) ? new MenuItem(this.name, this.path, this.allowedRoles) : null;
    }

    @Override
    public void render(String indent) {
        System.out.println(indent + "- " + name + " (path=" + path + ")");
    }
}

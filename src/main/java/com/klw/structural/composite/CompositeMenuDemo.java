package com.klw.structural.composite;

import java.util.Set;

public class CompositeMenuDemo {
    public static void main(String[] args) {
        // 构建样例菜单树
        MenuGroup root = new MenuGroup("后台管理");

        MenuGroup content = new MenuGroup("内容管理");
        content.add(new MenuItem("文章列表", "/articles", Set.of("editor", "admin")));
        content.add(new MenuItem("创建文章", "/articles/new", Set.of("editor", "admin")));
        content.add(new MenuItem("评论审核", "/comments", Set.of("moderator", "admin")));

        MenuGroup user = new MenuGroup("用户管理");
        user.add(new MenuItem("用户列表", "/users", Set.of("admin")));
        user.add(new MenuItem("角色权限", "/roles", Set.of("admin")));

        MenuGroup analytics = new MenuGroup("数据分析");
        analytics.add(new MenuItem("流量报表", "/reports/traffic", Set.of("analyst", "admin")));
        analytics.add(new MenuItem("转化分析", "/reports/conversion", Set.of("analyst", "admin")));

        root.add(content);
        root.add(user);
        root.add(analytics);

        // 用不同角色查看过滤后的菜单
        System.out.println("== admin 看到的菜单 ==");
        MenuComponent adminMenu = root.filterByRole("admin");
        if (adminMenu != null) adminMenu.render("");

        System.out.println("\n== editor 看到的菜单 ==");
        MenuComponent editorMenu = root.filterByRole("editor");
        if (editorMenu != null) editorMenu.render("");

        System.out.println("\n== analyst 看到的菜单 ==");
        MenuComponent analystMenu = root.filterByRole("analyst");
        if (analystMenu != null) analystMenu.render("");

        System.out.println("\n== guest 看到的菜单 ==");
        MenuComponent guestMenu = root.filterByRole("guest");
        if (guestMenu != null) guestMenu.render("");
        else System.out.println("(无可见菜单)");
    }
}

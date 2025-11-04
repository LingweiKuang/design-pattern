package com.klw.behavioral.visitor;

import com.klw.behavioral.visitor.element.DateField;
import com.klw.behavioral.visitor.element.NumberField;
import com.klw.behavioral.visitor.element.Section;
import com.klw.behavioral.visitor.element.TextField;

import java.time.LocalDate;

public class VisitorDemo {
    public static void main(String[] args) {
        Section root = new Section("report");

        root.add(new TextField("title", "月度报表"));
        root.add(new DateField("generatedAt", LocalDate.of(2025, 11, 4)));

        Section sectionA = new Section("sales");
        sectionA.add(new NumberField("total", 12345.67));
        sectionA.add(new NumberField("count", 250));
        sectionA.add(new TextField("note", "包含线上与线下"));

        Section sectionB = new Section("users");
        sectionB.add(new NumberField("active", 1024));
        sectionB.add(new TextField("region", "APAC"));

        root.add(sectionA);
        root.add(sectionB);

        // JSON 导出
        JsonExportVisitor jsonVisitor = new JsonExportVisitor();
        String json = jsonVisitor.getJson(root);
        System.out.println("=== JSON Export ===");
        System.out.println(json);

        // 统计
        StatisticsVisitor stats = new StatisticsVisitor();
        // TODO root.accept(this); 是访问者模式精髓
        root.accept(stats);
        System.out.println("=== Statistics ===");
        System.out.println(stats.report());
    }
}

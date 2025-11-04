package com.klw.behavioral.visitor.element;

import com.klw.behavioral.visitor.ReportVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 复合元素：Section（可包含其他元素）
public class Section implements ReportElement {
    private final String name;
    private final List<ReportElement> children = new ArrayList<>();

    public Section(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(ReportElement e) {
        children.add(e);
    }

    public List<ReportElement> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void accept(ReportVisitor visitor) {
        visitor.visit(this);                // 访问 Section 节点（可以用于输出开始标签）
        for (ReportElement e : children) {  // 遍历子元素并派发 accept -> 双重分发
            e.accept(visitor);
        }
        visitor.afterVisit(this);           // 可选的结束回调（例如闭合 JSON 对象）
    }
}

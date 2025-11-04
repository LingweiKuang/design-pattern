package com.klw.behavioral.visitor;

import com.klw.behavioral.visitor.element.DateField;
import com.klw.behavioral.visitor.element.NumberField;
import com.klw.behavioral.visitor.element.Section;
import com.klw.behavioral.visitor.element.TextField;

import java.util.ArrayDeque;
import java.util.Deque;

// -------- 访问者实现：JSON 导出 --------
public class JsonExportVisitor implements ReportVisitor {
    private final StringBuilder sb = new StringBuilder();
    private int indent = 0;
    private final Deque<Boolean> firstElementStack = new ArrayDeque<>();

    private void indent() {
        for (int i = 0; i < indent; i++) sb.append("  ");
    }

    private void commaIfNeeded() {
        if (!firstElementStack.isEmpty()) {
            boolean first = firstElementStack.peek();
            if (!first) sb.append(",\n");
            else sb.append("\n");
            firstElementStack.pop();
            firstElementStack.push(false);
        }
    }

    public void visit(Section s) {
        commaIfNeeded();
        indent();
        sb.append("\"").append(s.getName()).append("\": ");
        sb.append("{\n");
        indent++;
        firstElementStack.push(true); // 当前对象还没有字段
    }

    public void afterVisit(Section s) {
        sb.append("\n");
        indent--;
        indent();
        sb.append("}");
        // 弹栈由调用方维护（caller will handle comma）
        // 当顶层 section 结束时 nothing else
    }

    public void visit(TextField t) {
        commaIfNeeded();
        indent();
        sb.append("\"").append(t.getName()).append("\": ");
        sb.append("\"").append(escape(t.getText())).append("\"");
    }

    public void visit(NumberField n) {
        commaIfNeeded();
        indent();
        sb.append("\"").append(n.getName()).append("\": ");
        sb.append(n.getValue());
    }

    public void visit(DateField d) {
        commaIfNeeded();
        indent();
        sb.append("\"").append(d.getName()).append("\": ");
        sb.append("\"").append(d.getDate().toString()).append("\"");
    }

    private String escape(String s) {
        return s.replace("\"", "\\\"");
    }

    public String getJson(Section root) {
        sb.setLength(0);
        indent = 0;
        firstElementStack.clear();
        // Wrap with top-level braces
        sb.append("{\n");
        indent++;
        firstElementStack.push(true);
        root.accept(this);
        // after full traversal, close any remaining open objects
        // trigger afterVisit for root already called by Section.accept
        sb.append("\n");
        indent--;
        sb.append("}\n");
        return sb.toString();
    }
}

package com.klw.behavioral.visitor.element;

import com.klw.behavioral.visitor.ReportVisitor;

public class TextField implements ReportElement {
    private final String name;
    private final String text;

    public TextField(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void accept(ReportVisitor visitor) {
        visitor.visit(this);
    }
}

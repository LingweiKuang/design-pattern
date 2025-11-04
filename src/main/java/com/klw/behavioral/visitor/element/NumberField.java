package com.klw.behavioral.visitor.element;

import com.klw.behavioral.visitor.ReportVisitor;

public class NumberField implements ReportElement {
    private final String name;
    private final double value;

    public NumberField(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public void accept(ReportVisitor visitor) {
        visitor.visit(this);
    }
}

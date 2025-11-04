package com.klw.behavioral.visitor.element;

import com.klw.behavioral.visitor.ReportVisitor;

import java.time.LocalDate;

public class DateField implements ReportElement {
    private final String name;
    private final LocalDate date;

    public DateField(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void accept(ReportVisitor visitor) {
        visitor.visit(this);
    }
}

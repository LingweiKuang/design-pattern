package com.klw.behavioral.visitor.element;

import com.klw.behavioral.visitor.ReportVisitor;

// -------- 元素接口与具体元素 --------
public interface ReportElement {
    void accept(ReportVisitor visitor);

    String getName();
}

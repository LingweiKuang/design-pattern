package com.klw.behavioral.visitor;

import com.klw.behavioral.visitor.element.DateField;
import com.klw.behavioral.visitor.element.NumberField;
import com.klw.behavioral.visitor.element.Section;
import com.klw.behavioral.visitor.element.TextField;

// -------- 访问者接口 --------
public interface ReportVisitor {
    void visit(TextField t);

    void visit(NumberField n);

    void visit(DateField d);

    void visit(Section s);

    // 提供一个 afterVisit 钩子供容器型元素结束时使用（可选）
    default void afterVisit(Section s) { /* 可空实现 */ }
}

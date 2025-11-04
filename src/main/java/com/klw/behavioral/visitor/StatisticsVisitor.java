package com.klw.behavioral.visitor;

import com.klw.behavioral.visitor.element.DateField;
import com.klw.behavioral.visitor.element.NumberField;
import com.klw.behavioral.visitor.element.Section;
import com.klw.behavioral.visitor.element.TextField;

// -------- 访问者实现：统计（示例） --------
public class StatisticsVisitor implements ReportVisitor {
    int totalElements = 0;
    int textCount = 0;
    int numberCount = 0;
    double numberSum = 0.0;
    int dateCount = 0;

    public void visit(TextField t) {
        totalElements++;
        textCount++;
    }

    public void visit(NumberField n) {
        totalElements++;
        numberCount++;
        numberSum += n.getValue();
    }

    public void visit(DateField d) {
        totalElements++;
        dateCount++;
    }

    public void visit(Section s) {
        // Section 本身也算一个元素（可视需求忽略）
        totalElements++;
    }

    public void afterVisit(Section s) { /* 不需要 */ }

    public String report() {
        double avg = numberCount == 0 ? 0.0 : numberSum / numberCount;
        return String.format("elements=%d, texts=%d, numbers=%d(sum=%.2f,avg=%.2f), dates=%d",
                totalElements, textCount, numberCount, numberSum, avg, dateCount);
    }
}

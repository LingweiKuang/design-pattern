package com.klw.behavioral.chainofresponsibility;

import com.klw.behavioral.chainofresponsibility.entity.Decision;
import com.klw.behavioral.chainofresponsibility.entity.LoanApplication;
import com.klw.behavioral.chainofresponsibility.entity.RuleService;

// 人工客服节点（示例：可补充材料或拒绝）
public class CustomerServiceHandler extends Handler {
    @Override
    protected Decision process(LoanApplication app, RuleService rules) {
        System.out.println("CustomerService reviewing app=" + app.getId());
        if (app.getCreditScore() < 620) {
            app.setRejectReason("Low credit score (customer service)");
            return Decision.REJECT;
        }
        // 人工通过后转给主管审核（示例逻辑）
        return Decision.FORWARD;
    }
}

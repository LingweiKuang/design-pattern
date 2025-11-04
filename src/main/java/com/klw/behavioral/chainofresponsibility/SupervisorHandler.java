package com.klw.behavioral.chainofresponsibility;

import com.klw.behavioral.chainofresponsibility.entity.Decision;
import com.klw.behavioral.chainofresponsibility.entity.LoanApplication;
import com.klw.behavioral.chainofresponsibility.entity.RuleService;

// 风控主管节点
public class SupervisorHandler extends Handler {
    @Override
    protected Decision process(LoanApplication app, RuleService rules) {
        System.out.println("Supervisor reviewing app=" + app.getId());
        if (app.getAmount() > 200000) {
            app.setRejectReason("Amount exceeds supervisor limit");
            return Decision.REJECT;
        }
        // 通过并继续流转到法务（如果需要）
        return Decision.FORWARD;
    }
}

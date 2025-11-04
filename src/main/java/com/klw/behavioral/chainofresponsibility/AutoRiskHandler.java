package com.klw.behavioral.chainofresponsibility;

import com.klw.behavioral.chainofresponsibility.entity.Decision;
import com.klw.behavioral.chainofresponsibility.entity.LoanApplication;
import com.klw.behavioral.chainofresponsibility.entity.RuleService;

import java.util.UUID;

// 具体节点：自动风控
public class AutoRiskHandler extends Handler {
    @Override
    protected Decision process(LoanApplication app, RuleService rules) {
        RuleService.Rules r = rules.get();
        System.out.println("AutoRisk checking app=" + app.getId() + " score=" + app.getCreditScore() +
                " overdue=" + app.getOverdueCount());
        if (app.getOverdueCount() > r.getMaxOverdueAllowed()) {
            app.setRejectReason("Too many overdue records");
            return Decision.REJECT;
        }
        if (app.getCreditScore() >= r.getMinCreditScoreForAutoApprove() && app.getAmount() <= r.getAutoDisburseMaxAmount()) {
            // 自动放款
            app.setDisbursementId("DISB-" + UUID.randomUUID());
            return Decision.DIRECT_DISBURSE;
        }
        // 否则交给人工
        return Decision.FORWARD;
    }
}

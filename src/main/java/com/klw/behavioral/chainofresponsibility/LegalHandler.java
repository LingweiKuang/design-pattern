package com.klw.behavioral.chainofresponsibility;

import com.klw.behavioral.chainofresponsibility.entity.Decision;
import com.klw.behavioral.chainofresponsibility.entity.LoanApplication;
import com.klw.behavioral.chainofresponsibility.entity.RuleService;

import java.util.UUID;

// 法务节点
public class LegalHandler extends Handler {
    @Override
    protected Decision process(LoanApplication app, RuleService rules) {
        System.out.println("Legal checking app=" + app.getId());
        // 简单示例：所有超过一定阈值的贷款需法务确认
        if (app.getAmount() > 1000000) {
            app.setRejectReason("Requires legal approval (not supported)");
            return Decision.REJECT;
        }
        // 最终同意放款（示例）
        app.setDisbursementId("DISB-" + UUID.randomUUID());
        return Decision.APPROVE;
    }
}

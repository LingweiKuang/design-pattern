package com.klw.behavioral.chainofresponsibility;

import com.klw.behavioral.chainofresponsibility.entity.Decision;
import com.klw.behavioral.chainofresponsibility.entity.LoanApplication;
import com.klw.behavioral.chainofresponsibility.entity.RuleService;

import java.util.concurrent.atomic.AtomicReference;

// 链管理器：持有当前链头（可原子替换）
public class ChainManager {
    private final AtomicReference<Handler> head = new AtomicReference<>();

    void setChainHead(Handler h) {
        head.set(h);
        System.out.println("Chain updated. head=" + (h == null ? "null" : h.getClass().getSimpleName()));
    }

    Handler getChainHead() {
        return head.get();
    }

    // 执行审批（单次申请）
    public void process(LoanApplication app, RuleService rules) {
        long start = System.currentTimeMillis();
        Handler cur = head.get();
        Handler prev = null;
        while (cur != null) {
            Decision d = cur.handle(app, rules);
            Handler finalCur = cur;
            System.out.println("Node " + finalCur.getClass().getSimpleName() + " returned " + d + " for app=" + app.getId());
            if (d == Decision.REJECT) {
                System.out.println("Application REJECTED: " + app + " reason=" + app.getRejectReason());
                break;
            } else if (d == Decision.DIRECT_DISBURSE) {
                System.out.println("Direct disburse for app=" + app + " disburseId=" + app.getDisbursementId());
                break;
            } else if (d == Decision.APPROVE) {
                System.out.println("APPROVED (via node): " + app + " disburseId=" + app.getDisbursementId());
                break;
            } else { // FORWARD
                prev = cur;
                cur = cur.next;
            }
        }
        if (cur == null) {
            System.out.println("Reached end of chain without terminal decision for app=" + app.getId() + ", marking as pending.");
        }
        System.out.println("Processing finished for app=" + app.getId() + ", elapsed=" + (System.currentTimeMillis() - start) + "ms");
    }
}

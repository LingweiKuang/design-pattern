package com.klw.behavioral.chainofresponsibility;

import com.klw.behavioral.chainofresponsibility.entity.Decision;
import com.klw.behavioral.chainofresponsibility.entity.LoanApplication;
import com.klw.behavioral.chainofresponsibility.entity.RuleService;

public abstract class Handler {
    protected volatile Handler next; // 链可以在构建时连好
    protected volatile boolean enabled = true;

    public Handler setNext(Handler h) {
        this.next = h;
        return h;
    }

    public void setEnabled(boolean e) {
        this.enabled = e;
    }

    // 返回决策。如果返回 FORWARD 且 next != null 则继续；否则终止。
    public Decision handle(LoanApplication app, RuleService rules) {
        if (!enabled) {
            System.out.println(getClass().getSimpleName() + " disabled, forwarding. app=" + app.getId());
            return Decision.FORWARD;
        }
        return process(app, rules);
    }

    protected abstract Decision process(LoanApplication app, RuleService rules);
}

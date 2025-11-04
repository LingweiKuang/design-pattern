package com.klw.behavioral.mediator.service;

import com.klw.behavioral.mediator.Mediator;
import com.klw.behavioral.mediator.entity.CommandResult;
import com.klw.behavioral.mediator.entity.Event;

import java.util.Map;

public class PaymentAdapter extends ServiceAdapter {
    public PaymentAdapter(Mediator m, SagaStore saga) {
        super(m, saga, "payment");
    }

    @Override
    public void register() {
        mediator.subscribe("CMD.ChargePayment", ev -> {
            String orderId = ev.getTraceId();
            Map<String, Object> p = ev.getPayload();
            // simulate charging (random failure)
            boolean ok = Math.random() > 0.15;
            CommandResult res = ok ? CommandResult.ok("Charged") : CommandResult.fail("Payment declined");
            // record saga step if success
            if (ok) saga.markStep(orderId, "payment:charged");
            // respond to callback
            String cb = (String) p.get("_callbackTopic");
            if (cb != null) mediator.publish(new Event(cb, orderId, Map.of("result", res)));
        }, 50);
    }
}

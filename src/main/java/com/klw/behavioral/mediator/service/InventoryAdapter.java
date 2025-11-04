package com.klw.behavioral.mediator.service;

import com.klw.behavioral.mediator.Mediator;
import com.klw.behavioral.mediator.entity.CommandResult;
import com.klw.behavioral.mediator.entity.Event;

import java.util.Map;

public class InventoryAdapter extends ServiceAdapter {
    public InventoryAdapter(Mediator m, SagaStore saga) {
        super(m, saga, "inventory");
    }

    @Override
    public void register() {
        mediator.subscribe("CMD.LockInventory", ev -> {
            String orderId = ev.getTraceId();
            Map<String, Object> p = ev.getPayload();
            // simulate lock (fail sometimes)
            boolean ok = Math.random() > 0.12;
            CommandResult res = ok ? CommandResult.ok("Locked") : CommandResult.fail("Insufficient stock");
            if (ok) saga.markStep(orderId, "inventory:locked");
            if (p.get("_callbackTopic") != null)
                mediator.publish(new Event((String) p.get("_callbackTopic"), orderId, Map.of("result", res)));
        }, 40);

        // compensation: unlock inventory on "COMPENSATE.UnlockInventory"
        mediator.subscribe("CMD.UnlockInventory", ev -> {
            String orderId = ev.getTraceId();
            saga.markStep(orderId, "inventory:unlocked");
            if (ev.getPayload().get("_callbackTopic") != null)
                mediator.publish(new Event((String) ev.getPayload().get("_callbackTopic"), orderId, Map.of("result", CommandResult.ok("Unlocked"))));
        }, 40);
    }
}


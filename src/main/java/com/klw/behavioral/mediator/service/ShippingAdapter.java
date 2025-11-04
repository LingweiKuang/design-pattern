package com.klw.behavioral.mediator.service;

import com.klw.behavioral.mediator.Mediator;
import com.klw.behavioral.mediator.entity.CommandResult;
import com.klw.behavioral.mediator.entity.Event;

import java.util.Map;
import java.util.UUID;

public class ShippingAdapter extends ServiceAdapter {
    public ShippingAdapter(Mediator m, SagaStore saga) {
        super(m, saga, "shipping");
    }

    @Override
    public void register() {
        mediator.subscribe("CMD.CreateShipment", ev -> {
            String orderId = ev.getTraceId();
            boolean ok = Math.random() > 0.1;
            CommandResult res = ok ? CommandResult.ok("ShipmentCreated:SHIP-" + UUID.randomUUID()) : CommandResult.fail("Shipping provider error");
            if (ok) saga.markStep(orderId, "shipping:created");
            if (ev.getPayload().get("_callbackTopic") != null)
                mediator.publish(new Event((String) ev.getPayload().get("_callbackTopic"), orderId, Map.of("result", res)));
        }, 30);
    }
}


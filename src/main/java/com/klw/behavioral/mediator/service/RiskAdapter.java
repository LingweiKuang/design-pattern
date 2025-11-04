package com.klw.behavioral.mediator.service;

import com.klw.behavioral.mediator.Mediator;
import com.klw.behavioral.mediator.entity.Event;

import java.util.Map;

public class RiskAdapter extends ServiceAdapter {
    public RiskAdapter(Mediator m, SagaStore saga) {
        super(m, saga, "risk");
    }

    @Override
    public void register() {
        mediator.subscribe("Event.OrderCreated", ev -> {
            // for demo, randomly block tiny percent
            if (Math.random() < 0.05) {
                System.out.println("[Risk] Blocking order " + ev.getTraceId());
                // publish a block event
                mediator.publish(new Event("Event.OrderBlocked", ev.getTraceId(), Map.of("reason", "suspected fraud")));
            }
        }, 60);
    }
}


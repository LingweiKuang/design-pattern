package com.klw.behavioral.mediator.service;

import com.klw.behavioral.mediator.Mediator;

public class NotificationAdapter extends ServiceAdapter {
    public NotificationAdapter(Mediator m, SagaStore saga) {
        super(m, saga, "notification");
    }

    @Override
    public void register() {
        mediator.subscribe("Event.OrderCompleted", ev -> {
            // receive final event & send notification (no callback)
            System.out.println("[Notifier] Notifying user about order " + ev.getTraceId());
            saga.markStep(ev.getTraceId(), "notified:user");
        }, 10);
    }
}


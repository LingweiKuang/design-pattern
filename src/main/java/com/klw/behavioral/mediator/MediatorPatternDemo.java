package com.klw.behavioral.mediator;

import com.klw.behavioral.mediator.service.*;

import java.util.List;
import java.util.Map;

public class MediatorPatternDemo {
    public static void main(String[] args) throws InterruptedException {
        Mediator mediator = new Mediator();
        SagaStore saga = new SagaStore();

        // register adapters
        PaymentAdapter payment = new PaymentAdapter(mediator, saga);
        InventoryAdapter inventory = new InventoryAdapter(mediator, saga);
        ShippingAdapter shipping = new ShippingAdapter(mediator, saga);
        NotificationAdapter notify = new NotificationAdapter(mediator, saga);
        RiskAdapter risk = new RiskAdapter(mediator, saga);

        payment.register();
        inventory.register();
        shipping.register();
        notify.register();
        risk.register();

        // optional monitoring hook: log every publish
        mediator.onEveryEvent(ev -> System.out.println("[AuditHook] " + ev.getType() + " for " + ev.getTraceId()));

        OrderSagaCoordinator coordinator = new OrderSagaCoordinator(mediator, saga);

        // Start several order flows
        for (int i = 0; i < 6; i++) {
            String orderId = "ORD-" + i + "-" + System.currentTimeMillis();
            Map<String, Object> data = Map.of(
                    "amount", 100 + i * 10,
                    "items", List.of(Map.of("sku", "SKU-1", "qty", 1)),
                    "address", "Tokyo " + i
            );
            coordinator.startOrderFlow(orderId, data);
            Thread.sleep(100); // stagger starts
        }

        // Let the async system run for a bit
        Thread.sleep(4000);

        // Print audit
        System.out.println("==== AUDIT LOG ====");
        mediator.getAudit().forEach(System.out::println);

        // Print saga steps for an example order
        System.out.println("==== SAGA SAMPLE ====");
        System.out.println("Steps for ORD-0 (if exists): " + saga.getSteps("ORD-0"));

        mediator.shutdown();
    }
}

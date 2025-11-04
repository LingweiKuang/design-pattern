package com.klw.behavioral.mediator.service;

import com.klw.behavioral.mediator.Mediator;
import com.klw.behavioral.mediator.entity.CommandMessage;
import com.klw.behavioral.mediator.entity.Event;

import java.util.Map;

public class OrderSagaCoordinator {
    private final Mediator mediator;
    private final SagaStore saga;

    public OrderSagaCoordinator(Mediator mediator, SagaStore saga) {
        this.mediator = mediator;
        this.saga = saga;
    }

    // start order flow: publish Event.OrderCreated and run saga
    public void startOrderFlow(String orderId, Map<String, Object> orderData) {
        // initial event so risk etc can analyze
        mediator.publish(new Event("Event.OrderCreated", orderId, orderData));
        // orchestrate steps with commands and compensations
        // We'll use callbacks on each command to continue the saga; on failures issue compensations.
        // Step 1: Charge payment
        CommandMessage charge = new CommandMessage("ChargePayment", orderId, Map.of("amount", orderData.get("amount")));
        mediator.sendCommand(charge, res1 -> {
            if (!res1.isSuccess()) {
                System.out.println("[Saga] Payment failed for " + orderId + ", aborting.");
                mediator.publish(new Event("Event.OrderFailed", orderId, Map.of("reason", "payment_failed", "detail", res1.getMessage())));
                return;
            }
            System.out.println("[Saga] Payment charged for " + orderId);
            // Step 2: Lock inventory
            CommandMessage lock = new CommandMessage("LockInventory", orderId, Map.of("items", orderData.get("items")));
            mediator.sendCommand(lock, res2 -> {
                if (!res2.isSuccess()) {
                    System.out.println("[Saga] Inventory lock failed for " + orderId + ", triggering refund.");
                    // compensation: refund (for demo we just publish event)
                    mediator.publish(new Event("Event.RefundRequested", orderId, Map.of("reason", "inventory_failed")));
                    mediator.publish(new Event("Event.OrderFailed", orderId, Map.of("reason", "inventory_failed")));
                    return;
                }
                System.out.println("[Saga] Inventory locked for " + orderId);
                // Step 3: Create shipment
                CommandMessage ship = new CommandMessage("CreateShipment", orderId, Map.of("address", orderData.get("address")));
                mediator.sendCommand(ship, res3 -> {
                    if (!res3.isSuccess()) {
                        System.out.println("[Saga] Shipment failed for " + orderId + ", compensating: unlock inventory + refund.");
                        // compensation: unlock inventory (via command)
                        CommandMessage unlock = new CommandMessage("UnlockInventory", orderId, Map.of());
                        mediator.sendCommand(unlock, r -> {
                            mediator.publish(new Event("Event.RefundRequested", orderId, Map.of("reason", "shipment_failed")));
                            mediator.publish(new Event("Event.OrderFailed", orderId, Map.of("reason", "shipment_failed")));
                        });
                        return;
                    }
                    System.out.println("[Saga] Shipment created for " + orderId + ", completing order.");
                    // Finalize: publish completed event (other services subscribe)
                    mediator.publish(new Event("Event.OrderCompleted", orderId, Map.of("shipmentId", res3.getMessage())));
                });
            });
        });
    }
}

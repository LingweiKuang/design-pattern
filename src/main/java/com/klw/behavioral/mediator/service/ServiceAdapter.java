package com.klw.behavioral.mediator.service;

import com.klw.behavioral.mediator.Mediator;

// -------------------- Service Adapters (simulated implementations) --------------------
public abstract class ServiceAdapter {
    protected final Mediator mediator;
    protected final SagaStore saga;
    protected final String name;

    ServiceAdapter(Mediator m, SagaStore saga, String name) {
        this.mediator = m;
        this.saga = saga;
        this.name = name;
    }

    // register handlers (subscribe to commands)
    abstract void register();
}

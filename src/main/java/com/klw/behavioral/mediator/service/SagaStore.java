package com.klw.behavioral.mediator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Saga state keeper (very simple in-memory store)
public class SagaStore {
    private final Map<String, List<String>> steps = new ConcurrentHashMap<>(); // traceId -> steps done

    public void markStep(String traceId, String step) {
        steps.computeIfAbsent(traceId, k -> new ArrayList<>()).add(step);
    }

    public List<String> getSteps(String traceId) {
        return steps.getOrDefault(traceId, Collections.emptyList());
    }

    void clear(String traceId) {
        steps.remove(traceId);
    }
}

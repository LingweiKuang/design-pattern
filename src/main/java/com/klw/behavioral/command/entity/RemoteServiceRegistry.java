package com.klw.behavioral.command.entity;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// -------------简单的远程目标模拟（服务/配置状态）-------------
public class RemoteServiceRegistry {
    private final Map<String, Boolean> serviceRunning = new ConcurrentHashMap<>();
    private final Map<String, String> configs = new ConcurrentHashMap<>();
    // simulate idempotent remote operations via keys
    private final Set<String> processedIdempotencyKeys = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // Try start service; returns idempotent: second call with same key -> no-op success
    public CommandResult startService(String serviceName, String idempotencyKey) throws Exception {
        if (idempotencyKey != null && !processedIdempotencyKeys.add(idempotencyKey)) {
            System.out.println("startService idempotent replay detected: " + idempotencyKey);
            return CommandResult.ok("Already processed (idempotent): " + idempotencyKey);
        }
        // simulate remote call + possibility of transient failure
        maybeFailTransient("startService");
        serviceRunning.put(serviceName, true);
        return CommandResult.ok("Service " + serviceName + " started");
    }

    public CommandResult stopService(String serviceName, String idempotencyKey) throws Exception {
        if (idempotencyKey != null && !processedIdempotencyKeys.add(idempotencyKey)) {
            System.out.println("stopService idempotent replay detected: " + idempotencyKey);
            return CommandResult.ok("Already processed (idempotent): " + idempotencyKey);
        }
        maybeFailTransient("stopService");
        serviceRunning.put(serviceName, false);
        return CommandResult.ok("Service " + serviceName + " stopped");
    }

    // deploy config returns previous config so command can save it for undo
    public String deployConfig(String target, String newConfig, String idempotencyKey) throws Exception {
        if (idempotencyKey != null && !processedIdempotencyKeys.add(idempotencyKey)) {
            System.out.println("deployConfig idempotent replay detected: " + idempotencyKey);
            return configs.get(target); // idempotent: return current (which may already be newConfig)
        }
        maybeFailTransient("deployConfig");
        return configs.put(target, newConfig); // returns previous
    }

    private void maybeFailTransient(String op) throws Exception {
        // 20% transient failure for demo
        if (Math.random() < 0.2) throw new IOException("Transient network error in " + op);
    }

    boolean isRunning(String name) {
        return Boolean.TRUE.equals(serviceRunning.get(name));
    }

    String getConfig(String t) {
        return configs.get(t);
    }
}

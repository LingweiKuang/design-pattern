package com.klw.behavioral.mediator;

import com.klw.behavioral.command.entity.CommandResult;
import com.klw.behavioral.mediator.entity.CommandMessage;
import com.klw.behavioral.mediator.entity.Event;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

// -------------------- Mediator / EventBus --------------------
public class Mediator {
    private final Map<String, List<Subscriber>> subscribers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final ExecutorService worker = Executors.newFixedThreadPool(8);
    private final Set<String> dedup = Collections.newSetFromMap(new ConcurrentHashMap<>()); // simple dedup store
    private final List<String> auditLog = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong seq = new AtomicLong();

    // Subscriber with priority
    static class Subscriber {
        final int priority;
        final Consumer<Event> handler;

        Subscriber(int priority, Consumer<Event> handler) {
            this.priority = priority;
            this.handler = handler;
        }
    }

    // register subscriber for event type
    public void subscribe(String eventType, Consumer<Event> handler, int priority) {
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                .add(new Subscriber(priority, handler));
        // sort by priority descending
        subscribers.get(eventType).sort(Comparator.comparingInt((Subscriber s) -> s.priority).reversed());
    }

    public void unsubscribe(String eventType, Consumer<Event> handler) {
        List<Subscriber> list = subscribers.get(eventType);
        if (list == null) return;
        list.removeIf(s -> s.handler.equals(handler));
    }

    // publish immediately (async)
    public void publish(Event e) {
        publish(e, 0);
    }

    // publish with delayMillis
    public void publish(Event e, long delayMillis) {
        Runnable task = () -> worker.execute(() -> dispatch(e));
        if (delayMillis <= 0) scheduler.execute(task);
        else scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }

    // dispatch internal, with dedup & audit
    private void dispatch(Event e) {
        String dedupKey = e.getTraceId() + ":" + e.getType();
        // simple dedup: skip duplicate identical event within small window
        if (!dedup.add(dedupKey)) {
            audit("DUP_SKIPPED", e);
            return;
        }
        // record audit
        audit("PUBLISH", e);
        List<Subscriber> list = subscribers.getOrDefault(e.getType(), Collections.emptyList());
        for (Subscriber s : list) {
            try {
                s.handler.accept(e);
            } catch (Exception ex) {
                audit("HANDLER_ERROR", e, ex);
            }
        }
        // evict dedup after short time to allow replays later (demo)
        scheduler.schedule(() -> dedup.remove(dedupKey), 5, TimeUnit.SECONDS);
    }

    // send command to a specific handler (adapters can register command endpoints as subscribers to special topics)
    public void sendCommand(CommandMessage cmd, Consumer<CommandResult> onComplete) {
        // For this demo we publish a special event type "CMD.{type}" and expect service adapters subscribe to it.
        Event wrapper = new Event("CMD." + cmd.getType(), cmd.getTraceId(), cmd.getPayload());
        // use a future to capture completion via adapter invoking callback through mediator
        // we'll use a correlation id as traceId (same) and callbacks via a temporary subscriber
        String cbTopic = "CB." + cmd.getTraceId() + "." + seq.incrementAndGet();
        // subscribe to callback
        Consumer<Event> cbHandler = ev -> {
            // callback payload expected to contain "result" -> CommandResult
            Object res = ev.getPayload().get("result");
            if (res instanceof CommandResult) onComplete.accept((CommandResult) res);
        };
        subscribe(cbTopic, cbHandler, 100);
        // include callback topic in payload
        Map<String, Object> p = new HashMap<>(cmd.getPayload());
        p.put("_callbackTopic", cbTopic);
        Event e = new Event("CMD." + cmd.getType(), cmd.getTraceId(), p);
        publish(e);
        // schedule cleanup of callback subscriber after timeout
        scheduler.schedule(() -> unsubscribe(cbTopic, cbHandler), 10, TimeUnit.SECONDS);
    }

    private void audit(String action, Event e) {
        audit(action, e, null);
    }

    private void audit(String action, Event e, Exception ex) {
        String rec = String.format("%s | %s | %s | %s | %s", Instant.now(), action, e.getTraceId(), e.getType(), ex == null ? "OK" : ex.getMessage());
        auditLog.add(rec);
    }

    public List<String> getAudit() {
        return List.copyOf(auditLog);
    }

    public void shutdown() {
        scheduler.shutdownNow();
        worker.shutdownNow();
    }

    // monitoring hook simple example: register to all events by subscribing to "*" topic
    public void onEveryEvent(Consumer<Event> hook) {
        subscribe("*", hook, 0);
    }

    // dispatch wildcard support in dispatch (very simple): if subscriber exists for "*", call it
    // (we'll call it in dispatch after normal subscribers)
    private void dispatchWildcard(Event e) {
        List<Subscriber> list = subscribers.getOrDefault("*", Collections.emptyList());
        for (Subscriber s : list) {
            try {
                s.handler.accept(e);
            } catch (Exception ex) {
                audit("WILDCARD_HANDLER_ERROR", e, ex);
            }
        }
    }

    // alter dispatch to call wildcard as well
    private void dispatchWithWildcard(Event e) {
        dispatch(e); // existing logic (includes audit)
        dispatchWildcard(e);
    }
}

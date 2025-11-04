package com.klw.behavioral.iterator.entity;

import java.time.Instant;

public class Event implements Comparable<Event> {
    final String id;
    final Instant ts;
    final String payload;

    public Event(String id, Instant ts, String payload) {
        this.id = id;
        this.ts = ts;
        this.payload = payload;
    }

    @Override
    public int compareTo(Event o) {
        return this.ts.compareTo(o.ts);
    }

    @Override
    public String toString() {
        return "Event{" + id + "," + ts + "," + payload + "}";
    }
}

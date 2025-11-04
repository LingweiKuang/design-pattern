package com.klw.behavioral.mediator.entity;

import com.klw.behavioral.mediator.Message;
import lombok.Getter;

import java.util.Map;

@Getter
public class Event implements Message {
    final String type;
    final String traceId; // order id or correlation id
    final Map<String, Object> payload;

    public Event(String type, String traceId, Map<String, Object> payload) {
        this.type = type;
        this.traceId = traceId;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public String getTraceId() {
        return traceId;
    }

    public String toString() {
        return "Event{" + type + "," + traceId + "," + payload + "}";
    }
}

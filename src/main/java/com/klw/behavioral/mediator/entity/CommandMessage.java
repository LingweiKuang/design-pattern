package com.klw.behavioral.mediator.entity;

import com.klw.behavioral.mediator.Message;
import lombok.Getter;

import java.util.Map;

@Getter
public class CommandMessage implements Message {
    final String type;
    final String traceId;
    final Map<String, Object> payload;

    public CommandMessage(String type, String traceId, Map<String, Object> payload) {
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
        return "Cmd{" + type + "," + traceId + "," + payload + "}";
    }
}

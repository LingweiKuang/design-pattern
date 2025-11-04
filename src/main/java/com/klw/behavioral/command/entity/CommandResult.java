package com.klw.behavioral.command.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class CommandResult implements Serializable {
    final boolean success;
    final String message;
    final Instant timestamp = Instant.now();

    public CommandResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static CommandResult ok(String msg) {
        return new CommandResult(true, msg);
    }

    public static CommandResult fail(String msg) {
        return new CommandResult(false, msg);
    }

    public String toString() {
        return "[" + (success ? "OK" : "FAIL") + "] " + message + " @" + timestamp;
    }
}

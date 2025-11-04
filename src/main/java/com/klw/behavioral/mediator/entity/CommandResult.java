package com.klw.behavioral.mediator.entity;

// -------------------- Command result and simple saga state --------------------
public class CommandResult {
    final boolean success;
    final String message;

    CommandResult(boolean success, String message) {
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
        return "[" + (success ? "OK" : "FAIL") + "] " + message;
    }
}

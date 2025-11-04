package com.klw.behavioral.command;

import com.klw.behavioral.command.entity.CommandResult;

import java.util.UUID;

public abstract class BaseCommand implements Command {
    private final String id = UUID.randomUUID().toString();
    private final String name;

    BaseCommand(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // default undo does nothing (non-reversible)
    public boolean isReversible() {
        return false;
    }

    public CommandResult undo() throws Exception {
        return CommandResult.fail("Not reversible");
    }
}

package com.klw.behavioral.command;

import com.klw.behavioral.command.entity.CommandResult;

import java.io.Serializable;

// 命令
public interface Command extends Serializable {
    String getId();

    boolean isReversible();

    CommandResult execute() throws Exception;

    CommandResult undo() throws Exception;

    default String getName() {
        return this.getClass().getSimpleName();
    }
}

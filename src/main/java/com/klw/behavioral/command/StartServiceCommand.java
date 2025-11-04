package com.klw.behavioral.command;

import com.klw.behavioral.command.entity.CommandEngine;
import com.klw.behavioral.command.entity.CommandResult;

import java.util.UUID;

// 启动服务命令
public class StartServiceCommand extends BaseCommand {
    private final String serviceName;
    private final String idempotencyKey;

    // we don't store much local state; undo will call stop
    StartServiceCommand(String serviceName, String idempotencyKey) {
        super("StartService:" + serviceName);
        this.serviceName = serviceName;
        this.idempotencyKey = idempotencyKey;
    }

    public boolean isReversible() {
        return true;
    }

    @Override
    public CommandResult execute() throws Exception {
        return CommandEngine.getRegistry().startService(serviceName, idempotencyKey);
    }

    @Override
    public CommandResult undo() throws Exception {
        return CommandEngine.getRegistry().stopService(serviceName, UUID.randomUUID().toString()); // new idempotency key for undo
    }

}

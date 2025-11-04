package com.klw.behavioral.command;

import com.klw.behavioral.command.entity.CommandEngine;
import com.klw.behavioral.command.entity.CommandResult;

import java.util.UUID;

// 部署配置命令
public class DeployConfigCommand extends BaseCommand {
    private final String target;
    private final String newConfig;
    // transient prev saved at execution time (must be Serializable if persisted)
    private String previousConfig;
    // 幂等 KEY
    private final String idempotencyKey;

    DeployConfigCommand(String target, String newConfig, String idempotencyKey) {
        super("DeployConfig:" + target);
        this.target = target;
        this.newConfig = newConfig;
        this.idempotencyKey = idempotencyKey;
    }

    public boolean isReversible() {
        return true;
    }

    @Override
    public CommandResult execute() throws Exception {
        previousConfig = CommandEngine.getRegistry().deployConfig(target, newConfig, idempotencyKey);
        return CommandResult.ok("Deployed config; previous=" + previousConfig);
    }

    @Override
    public CommandResult undo() throws Exception {
        // restore previous config (may be null)
        String key = UUID.randomUUID().toString();
        String prev = previousConfig;
        CommandEngine.getRegistry().deployConfig(target, prev, key);
        return CommandResult.ok("Rolled back config to previous=" + prev);
    }

}

package com.klw.behavioral.command;

import com.klw.behavioral.command.entity.CommandEngine;
import com.klw.behavioral.command.entity.CommandResult;

import java.util.Arrays;
import java.util.List;

// 命令模式演示
public class CommandPatternDemo {
    public static void main(String[] args) throws Exception {
        CommandEngine engine = new CommandEngine(4, 3, 200);
        // create commands
        Command startA = new StartServiceCommand("payment-service", "idem-start-payment-1");
        Command deployCfg = new DeployConfigCommand("payment-service", "{\"featureX\":true}", "idem-deploy-1");

        // 1) async single submit
        engine.submitAsync(startA).thenAccept(r -> System.out.println("Async startA result: " + r));

        // 2) transactional batch (if one fails, previous undone)
        List<Command> batch = Arrays.asList(
                new StartServiceCommand("svc-1", "idem-svc-1"),
                new DeployConfigCommand("svc-1", "{\"v\":2}", "idem-deploy-svc1"),
                new StartServiceCommand("svc-2", "idem-svc-2")
        );

        engine.submitTransactionalBatchAsync(batch).thenAccept(r -> {
            System.out.println("Batch result: " + r);
            System.out.println("Audit sample:");
            engine.getAuditLog().forEach(System.out::println);
        });

        // Wait a bit for async tasks
        Thread.sleep(3000);

        // 3) undo last reversible command (if any)
        CommandResult undoRes = engine.undoLast();
        System.out.println("Undo last: " + undoRes);

        // 4) redo
        CommandResult redoRes = engine.redoLast();
        System.out.println("Redo last: " + redoRes);

        engine.shutdown();
    }
}

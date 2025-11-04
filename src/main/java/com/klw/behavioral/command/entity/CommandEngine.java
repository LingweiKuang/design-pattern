package com.klw.behavioral.command.entity;

import com.klw.behavioral.command.Command;
import lombok.Getter;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

// -------------命令引擎（Invoker / Scheduler）-------------
public class CommandEngine {
    // shared remote registry (simulated remote)
    @Getter
    static final RemoteServiceRegistry registry = new RemoteServiceRegistry();

    private final ExecutorService pool;
    // undo / redo stacks (in-memory demo)
    private final Deque<Command> undoStack = new LinkedList<>();
    private final Deque<Command> redoStack = new LinkedList<>();
    // TODO 注意: synchronized 和 ReentrantLock 互不干扰
    private final ReentrantLock lock = new ReentrantLock(); // redoStack 和 undoStack 共享锁
    // audit trail
    private final List<String> audit = Collections.synchronizedList(new ArrayList<>());
    // simple retry config
    private final int maxRetries;
    private final long retryBackoffMillis;

    public CommandEngine(int threads, int maxRetries, long retryBackoffMillis) {
        this.pool = Executors.newFixedThreadPool(threads);
        this.maxRetries = maxRetries;
        this.retryBackoffMillis = retryBackoffMillis;
    }

    // submit single command async
    public CompletableFuture<CommandResult> submitAsync(Command cmd) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                CommandResult res = executeWithRetry(cmd);
                recordAudit(cmd, res);
                if (res.success && cmd.isReversible()) {
                    lock.lock();
                    try {
                        undoStack.push(cmd);
                        redoStack.clear();
                    } finally {
                        lock.unlock();
                    }
                }
                return res;
            } catch (Exception e) {
                CommandResult fail = CommandResult.fail("Exception: " + e.getMessage());
                recordAudit(cmd, fail);
                return fail;
            }
        }, pool);
    }

    // synchronous execution (blocking)
    public CommandResult submit(Command cmd) {
        try {
            CommandResult res = executeWithRetry(cmd);
            recordAudit(cmd, res);
            if (res.success && cmd.isReversible()) {
                lock.lock();
                try {
                    undoStack.push(cmd);
                    redoStack.clear();
                } finally {
                    lock.unlock();
                }
            }
            return res;
        } catch (Exception e) {
            CommandResult fail = CommandResult.fail("Exception: " + e.getMessage());
            recordAudit(cmd, fail);
            return fail;
        }
    }

    // transactional batch: execute sequentially; if any fails -> undo executed ones in reverse order
    public CompletableFuture<CommandResult> submitTransactionalBatchAsync(List<Command> commands) {
        return CompletableFuture.supplyAsync(() -> {
            List<Command> executed = new ArrayList<>();
            try {
                for (Command c : commands) {
                    CommandResult r = executeWithRetry(c);
                    recordAudit(c, r);
                    if (!r.success) throw new RuntimeException("Command failed: " + r.message);
                    if (c.isReversible()) executed.add(c);
                }
                // on success, push reversible ones to undo stack
                lock.lock();
                try {
                    for (Command c : executed) undoStack.push(c);
                    redoStack.clear();
                } finally {
                    lock.unlock();
                }
                return CommandResult.ok("Batch executed successfully");
            } catch (Exception e) {
                System.out.println("Batch failed; rolling back: " + e.getMessage());
                // undo in reverse
                for (int i = executed.size() - 1; i >= 0; i--) {
                    Command c = executed.get(i);
                    try {
                        CommandResult ur = c.undo();
                        recordAudit(c, new CommandResult(ur.success, "UNDO: " + ur.message));
                    } catch (Exception ex) {
                        recordAudit(c, CommandResult.fail("UNDO failed: " + ex.getMessage()));
                    }
                }
                return CommandResult.fail("Batch failed and rolled back: " + e.getMessage());
            }
        }, pool);
    }

    // redo: pop from redoStack (under lock), execute outside lock, then push to undoStack (under lock) if success
    public CommandResult redoLast() {
        // TODO 注意：尽量不要在持锁期间执行耗时操作（比如 executeWithRetry()）
        // 通常做法是：在锁内弹出命令，然后释放锁；执行命令（耗时）后，再重获锁将命令放入目标栈（如果需要）。
        Command cmd;
        lock.lock();
        try {
            if (redoStack.isEmpty()) return CommandResult.fail("Redo stack empty");
            cmd = redoStack.pop();
        } finally {
            lock.unlock();
        }

        // 执行（可能耗时）在锁外
        CommandResult res;
        try {
            res = executeWithRetry(cmd);
            recordAudit(cmd, new CommandResult(res.success, "REDO: " + res.message));
        } catch (Exception e) {
            CommandResult fail = CommandResult.fail("Redo exception: " + e.getMessage());
            recordAudit(cmd, fail);
            return fail;
        }

        // 根据结果把 cmd 放回 undoStack（需要在锁内）
        if (res.success) {
            lock.lock();
            try {
                undoStack.push(cmd);
            } finally {
                lock.unlock();
            }
        }
        return res;
    }

    // undo: 同上逻辑（先弹出、执行 undo、再在锁内把cmd放到redoStack）
    public CommandResult undoLast() {
        Command cmd;
        lock.lock();
        try {
            if (undoStack.isEmpty()) return CommandResult.fail("Undo stack empty");
            cmd = undoStack.pop();
        } finally {
            lock.unlock();
        }

        CommandResult res;
        try {
            res = cmd.undo();
            recordAudit(cmd, new CommandResult(res.success, "UNDO: " + res.message));
        } catch (Exception e) {
            CommandResult fail = CommandResult.fail("Undo exception: " + e.getMessage());
            recordAudit(cmd, fail);
            return fail;
        }

        if (res.success) {
            lock.lock();
            try {
                redoStack.push(cmd);
            } finally {
                lock.unlock();
            }
        }
        return res;
    }

    private CommandResult executeWithRetry(Command cmd) throws Exception {
        int attempt = 0;
        while (true) {
            try {
                attempt++;
                System.out.println("Executing cmd " + cmd.getName() + " id=" + cmd.getId() + " attempt=" + attempt);
                return cmd.execute();
            } catch (Exception e) {
                System.out.println("Cmd " + cmd.getName() + " failed attempt=" + attempt + " err=" + e.getMessage());
                if (attempt > maxRetries) throw e;
                Thread.sleep(retryBackoffMillis * attempt);
            }
        }
    }

    private void recordAudit(Command c, CommandResult r) {
        String rec = String.join(" | ",
                Instant.now().toString(),
                c.getId(),
                c.getName(),
                String.valueOf(r.success),
                r.message);
        audit.add(rec);
    }

    public List<String> getAuditLog() {
        return List.copyOf(audit);
    }

    // serialize command to bytes (for persistence/transport)
    public static byte[] serialize(Command c) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(c);
            oos.flush();
            return bos.toByteArray();
        }
    }

    public static Command deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Command) ois.readObject();
        }
    }

    public void shutdown() {
        pool.shutdown();
    }
}

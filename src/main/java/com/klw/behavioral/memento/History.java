package com.klw.behavioral.memento;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Caretaker：有限历史（最多 N 条）且支持 undo/redo
 */
public class History {
    private final int capacity;
    // undoStack 的尾部为最新。我们用 deque 以便丢弃最旧记录。
    private final Deque<Memento> undoStack = new LinkedList<>();
    private final Deque<Memento> redoStack = new LinkedList<>();

    public History(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity>=1");
        this.capacity = capacity;
    }

    // 保存新快照（会清空 redo）
    public void push(Memento m) {
        // 如果新加入后超过容量，移除最旧（队首）
        if (undoStack.size() == capacity) {
            undoStack.removeFirst();
        }
        undoStack.addLast(m);
        redoStack.clear();
    }

    // 返回用于 restore 的上一个快照（或 null）
    public Memento undo() {
        if (undoStack.size() <= 1) return null; // 无前一状态可退回
        Memento current = undoStack.removeLast();
        redoStack.addLast(current);
        return undoStack.peekLast(); // 返回新的当前（即上一个）
    }

    public Memento redo() {
        if (redoStack.isEmpty()) return null;
        Memento m = redoStack.removeLast();
        // push 到 undoStack（也要尊重容量）
        if (undoStack.size() == capacity) {
            undoStack.removeFirst();
        }
        undoStack.addLast(m);
        return m;
    }

    // 方便初始化：把初始状态作为第一条记录
    public boolean isEmpty() {
        return undoStack.isEmpty();
    }
}

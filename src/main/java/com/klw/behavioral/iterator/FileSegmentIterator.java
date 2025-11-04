package com.klw.behavioral.iterator;

import com.klw.behavioral.iterator.entity.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

// 本地分段实现（模拟分段文件或分片）
public class FileSegmentIterator implements SegmentIterator<Event> {
    private final List<Event> events;
    private int index;
    private final String segmentId;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    FileSegmentIterator(String segmentId, List<Event> events, int startOffset) {
        this.segmentId = segmentId;
        this.events = new ArrayList<>(events);
        this.index = Math.max(0, startOffset);
    }

    @Override
    public boolean hasNext() {
        return !closed.get() && index < events.size();
    }

    @Override
    public Event next() {
        if (!hasNext()) throw new NoSuchElementException();
        return events.get(index++);
    }

    @Override
    public Event peek() {
        return hasNext() ? events.get(index) : null;
    }

    @Override
    public void close() {
        closed.set(true);
    }

    @Override
    public String getCheckpoint() {
        return segmentId + ":" + index;
    }

    @Override
    public boolean supportsCheckpoint() {
        return true;
    }
}

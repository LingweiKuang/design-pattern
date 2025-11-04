package com.klw.behavioral.iterator;

import com.klw.behavioral.iterator.entity.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

// MergedIterator：合并多个已经按 ts 升序输出的 iterator，返回全局有序流
public class MergedIterator implements SegmentIterator<Event> {
    private static class Holder implements Comparable<Holder> {
        final Event head;
        final SegmentIterator<Event> it;

        Holder(Event head, SegmentIterator<Event> it) {
            this.head = head;
            this.it = it;
        }

        @Override
        public int compareTo(Holder o) {
            return this.head.compareTo(o.head);
        }
    }

    private final PriorityQueue<Holder> pq = new PriorityQueue<>();
    private final List<SegmentIterator<Event>> sources;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    MergedIterator(List<SegmentIterator<Event>> sources) {
        this.sources = new ArrayList<>(sources);
        // initialize PQ with each source's peek if available
        for (SegmentIterator<Event> s : sources) {
            Event e = safePeek(s);
            if (e != null) {
                pq.offer(new Holder(e, s));
            }
        }
    }

    private static Event safePeek(SegmentIterator<Event> s) {
        try {
            return s.peek();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public boolean hasNext() {
        return !pq.isEmpty();
    }

    @Override
    public Event next() {
        if (!hasNext()) throw new NoSuchElementException();
        Holder h = pq.poll();
        // consume the head from its iterator
        Event consumed = h.it.next(); // should correspond to h.head
        // fetch next from that iterator and push back to pq
        Event next = safePeek(h.it);
        if (next != null) pq.offer(new Holder(next, h.it));
        return consumed;
    }

    @Override
    public Event peek() {
        if (!hasNext()) return null;
        return pq.peek().head;
    }

    @Override
    public void close() {
        if (closed.getAndSet(true)) return;
        for (SegmentIterator<Event> s : sources) {
            try {
                s.close();
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public String getCheckpoint() {
        // produce combined checkpoint: join each source checkpoint with ';'
        StringBuilder sb = new StringBuilder();
        for (SegmentIterator<Event> s : sources) {
            if (s.supportsCheckpoint()) {
                if (sb.length() > 0) sb.append(";");
                sb.append(s.getCheckpoint());
            }
        }
        return sb.toString();
    }

    @Override
    public boolean supportsCheckpoint() {
        for (SegmentIterator<Event> s : sources) if (!s.supportsCheckpoint()) return false;
        return true;
    }
}

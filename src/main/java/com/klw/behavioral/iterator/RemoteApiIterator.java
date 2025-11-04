package com.klw.behavioral.iterator;

import com.klw.behavioral.iterator.entity.Event;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

// 模拟远端 API 分页迭代器（带简单重试 & 断点 token）
// 在真实场景，fetchPage 会做 HTTP 请求并解析分页 token
public class RemoteApiIterator implements SegmentIterator<Event> {
    private final String streamId;
    private String nextToken; // 表示断点 token（null 表示从头）
    private final Queue<Event> buffer = new ArrayDeque<>();
    private final int pageSize;
    private final int maxRetries;
    private final long retryBackoffMillis;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    RemoteApiIterator(String streamId, String startToken, int pageSize, int maxRetries, long retryBackoffMillis) {
        this.streamId = streamId;
        this.nextToken = startToken;
        this.pageSize = pageSize;
        this.maxRetries = maxRetries;
        this.retryBackoffMillis = retryBackoffMillis;
    }

    // 模拟远端 page fetch（产生 events 并返回下一个 token）
    // 在真实代码中，此方法会发 HTTP 请求并返回结果，可能抛出 IOException
    private Pair<List<Event>, String> fetchPage(String token) throws Exception {
        // simulate transient failure
        if (Math.random() < 0.12) throw new IOException("Transient network error fetching page");
        // For demo: token is an integer offset as string
        int offset = token == null ? 0 : Integer.parseInt(token);
        List<Event> page = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            int idx = offset + i;
            Instant ts = Instant.now().minusSeconds((long) (Math.random() * 3600)).plusSeconds(idx);
            page.add(new Event(streamId + "-" + idx, ts, "payload-" + idx));
        }
        String newToken = Integer.toString(offset + pageSize);
        return new Pair<>(page, newToken);
    }

    private void ensureBuffer() {
        if (!buffer.isEmpty() || closed.get()) return;
        int attempt = 0;
        while (attempt <= maxRetries) {
            try {
                attempt++;
                Pair<List<Event>, String> r = fetchPage(nextToken);
                r.first.forEach(buffer::offer);
                nextToken = r.second;
                return;
            } catch (Exception ex) {
                if (attempt > maxRetries) {
                    // treat as end or propagate: for demo we stop iteration
                    closed.set(true);
                    return;
                }
                try {
                    Thread.sleep(retryBackoffMillis * attempt);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        ensureBuffer();
        return !buffer.isEmpty() && !closed.get();
    }

    @Override
    public Event next() {
        if (!hasNext()) throw new NoSuchElementException();
        return buffer.poll();
    }

    @Override
    public Event peek() {
        ensureBuffer();
        return buffer.peek();
    }

    @Override
    public void close() {
        closed.set(true);
        buffer.clear();
    }

    @Override
    public String getCheckpoint() {
        return streamId + ":" + (nextToken == null ? "END" : nextToken);
    }

    @Override
    public boolean supportsCheckpoint() {
        return true;
    }

    // small helper
    static class Pair<A, B> {
        final A first;
        final B second;

        Pair(A a, B b) {
            first = a;
            second = b;
        }
    }
}

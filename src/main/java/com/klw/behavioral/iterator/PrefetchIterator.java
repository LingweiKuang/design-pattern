package com.klw.behavioral.iterator;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

// PrefetchIterator：包装任一 SegmentIterator，做异步批量预取到内存队列
public class PrefetchIterator<T> implements SegmentIterator<T> {
    private final SegmentIterator<T> inner;
    private final BlockingQueue<T> queue;
    private final ExecutorService prefetchPool;
    private final int batchSize;
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private Future<?> prefetchTask;

    PrefetchIterator(SegmentIterator<T> inner, int batchSize, int queueCapacity, ExecutorService pool) {
        this.inner = inner;
        this.batchSize = Math.max(1, batchSize);
        this.queue = new ArrayBlockingQueue<>(Math.max(queueCapacity, batchSize * 2));
        this.prefetchPool = pool;
        startPrefetch();
    }

    private void startPrefetch() {
        prefetchTask = prefetchPool.submit(() -> {
            try {
                while (!closed.get()) {
                    // fill up to batchSize
                    int fetched = 0;
                    while (fetched < batchSize && inner.hasNext()) {
                        T item = inner.next();
                        // block if queue full (backpressure)
                        queue.put(item);
                        fetched++;
                    }
                    if (fetched == 0) {
                        // no more immediate data -> sleep briefly to avoid busy loop, then check again
                        if (!inner.hasNext()) break;
                        Thread.sleep(50);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // prefetch finished
            }
        });
    }

    @Override
    public boolean hasNext() {
        if (!queue.isEmpty()) return true;
        if (closed.get()) return false;
        return !queue.isEmpty() || inner.hasNext() || (prefetchTask != null && !prefetchTask.isDone());
    }

    @Override
    public T next() {
        try {
            T v = queue.poll(500, TimeUnit.MILLISECONDS);
            if (v != null) return v;
            if (!hasNext()) throw new NoSuchElementException();
            v = queue.poll();
            if (v == null) throw new NoSuchElementException();
            return v;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NoSuchElementException();
        }
    }

    @Override
    public T peek() {
        T v = queue.peek();
        if (v != null) return v;
        // if queue empty, try to load one synchronously from inner
        if (inner.hasNext()) {
            try {
                T item = inner.next();
                queue.offer(item);
                return item;
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        closed.set(true);
        if (prefetchTask != null) prefetchTask.cancel(true);
        inner.close();
    }

    @Override
    public String getCheckpoint() {
        return inner.getCheckpoint();
    }

    @Override
    public boolean supportsCheckpoint() {
        return inner.supportsCheckpoint();
    }
}

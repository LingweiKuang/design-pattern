package com.klw.behavioral.iterator;

import com.klw.behavioral.iterator.entity.Event;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IteratorPatternDemo {
    public static void main(String[] args) throws Exception {
        // 构造三个本地分片（file-like）
        List<Event> segA = new ArrayList<>(), segB = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            segA.add(new Event("A-" + i, Instant.now().minusSeconds(2000 - i * 10), "a" + i));
            segB.add(new Event("B-" + i, Instant.now().minusSeconds(1500 - i * 8), "b" + i));
        }
        FileSegmentIterator itA = new FileSegmentIterator("segA", segA, 0);
        FileSegmentIterator itB = new FileSegmentIterator("segB", segB, 0);

        // 一个远端分片（模拟），用 startToken = "0"
        RemoteApiIterator remote = new RemoteApiIterator("remote", "0", 5, 3, 200);

        // 包装预取器（线程池复用）
        ExecutorService prefetchPool = Executors.newFixedThreadPool(3);
        PrefetchIterator<Event> pA = new PrefetchIterator<>(itA, 5, 50, prefetchPool);
        PrefetchIterator<Event> pB = new PrefetchIterator<>(itB, 5, 50, prefetchPool);
        PrefetchIterator<Event> pR = new PrefetchIterator<>(remote, 5, 50, prefetchPool);

        // 合并迭代器（按时间升序）
        MergedIterator merged = new MergedIterator(Arrays.asList(pA, pB, pR));

        // 消费示例：按时间合并打印前 30 条
        int count = 0;
        while (merged.hasNext() && count < 30) {
            Event e = merged.next();
            System.out.println("Consumed: " + e);
            count++;
        }

        // 展示 checkpoint（断点续读 token）
        System.out.println("Checkpoint token: " + merged.getCheckpoint());

        // 关闭所有迭代器和线程池
        merged.close();
        prefetchPool.shutdownNow();
    }
}

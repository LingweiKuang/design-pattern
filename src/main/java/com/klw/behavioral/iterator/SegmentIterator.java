package com.klw.behavioral.iterator;

import java.io.Closeable;
import java.util.Iterator;

// 增强迭代器接口：支持 peek, checkpoint, close
// TODO hasNext() 和 next() 为迭代器核心要点
public interface SegmentIterator<T> extends Iterator<T>, Closeable {
    T peek(); // 查看下一个元素但不消费（返回 null 表示没有）

    // checkpoint 可用于断点续读，返回一个可序列化字符串（比如 offset / token）
    String getCheckpoint();

    boolean supportsCheckpoint();
}

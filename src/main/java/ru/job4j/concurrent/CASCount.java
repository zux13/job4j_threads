package ru.job4j.concurrent;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class CASCount {
    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        int current;
        do {
            current = count.get();
        } while (!count.compareAndSet(current, current + 1));
    }

    public int get() {
        return count.get();
    }
}
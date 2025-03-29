package ru.job4j.collection;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();
    @GuardedBy("this")
    private final int size;

    public SimpleBlockingQueue(int size) {
        this.size = size;
    }

    public synchronized void offer(T value) {
        while (queue.size() >= size) {
            try {
                System.out.println(Thread.currentThread().getName() + " is waiting for consumer");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(Thread.currentThread().getName() + " is interrupted.");
                return;
            }
        }
        queue.add(value);
        System.out.println(Thread.currentThread().getName() + " has added value " + value);
        notifyAll();
    }

    public synchronized T poll() {
        while (queue.isEmpty()) {
            try {
                System.out.println(Thread.currentThread().getName() + " is waiting for supply");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(Thread.currentThread().getName() + " was interrupted");
                return null;
            }
        }
        T value = queue.poll();
        System.out.println(Thread.currentThread().getName() + " is polling value " + value);
        notifyAll();
        return value;
    }
}

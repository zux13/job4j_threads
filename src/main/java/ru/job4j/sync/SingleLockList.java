package ru.job4j.sync;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {
    @GuardedBy("this")
    private final List<T> list;

    public SingleLockList(List<T> list) {
        this.list = copy(list);
    }

    public synchronized void add(T value) {
        list.add(value);
    }

    public synchronized T get(int index) {
        return list.get(index);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return copy(list).iterator();
    }

    private synchronized List<T> copy(List<T> origin) {
        /* Используется поверхностное клонирование */
        return new ArrayList<>(origin);
    }
}



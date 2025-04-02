package ru.job4j.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearch<T> extends RecursiveTask<Integer> {

    private final T[] array;
    private final T value;
    private final int from;
    private final int to;

    public ParallelSearch(T[] array, T value, int from, int to) {
        this.array = array;
        this.value = value;
        this.from = from;
        this.to = to;
    }

    public static <T> Integer search(T[] array, T value) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        return forkJoinPool.invoke(new ParallelSearch<>(array, value, 0, array.length - 1));
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            return findElement();
        }

        int middle = (from + to) / 2;
        ParallelSearch<T> leftSearch =  new ParallelSearch<>(array, value, from, middle);
        ParallelSearch<T> rightSearch =  new ParallelSearch<>(array, value, middle + 1, to);

        leftSearch.fork();
        int right = rightSearch.compute();
        int left = leftSearch.join();

        return Math.max(left, right);
    }

    private Integer findElement() {
        int result = -1;
        for (int i = from; i <= to; i++) {
            if (array[i].equals(value)) {
                result = i;
                break;
            }
        }
        return result;
    }
}

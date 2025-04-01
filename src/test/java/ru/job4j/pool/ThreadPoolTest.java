package ru.job4j.pool;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.assertj.core.api.Assertions.assertThat;

class ThreadPoolTest {

    @Test
    void whenExecuteTasksThenAllAreProcessed() throws InterruptedException {
        ThreadPool pool = new ThreadPool();
        List<Integer> results = new ArrayList<>();
        int taskCount = 10;

        for (int i = 0; i < taskCount; i++) {
            int num = i;
            pool.work(() -> {
                synchronized (results) {
                    results.add(num);
                }
            });
        }

        Thread.sleep(1000);
        pool.shutdown();

        assertThat(results).hasSize(taskCount);
    }

    @Test
    void whenExecuteTasksInParallelThenProcessedByMultipleThreads() throws InterruptedException {
        ThreadPool pool = new ThreadPool();
        AtomicInteger counter = new AtomicInteger(0);
        int taskCount = 20;

        for (int i = 0; i < taskCount; i++) {
            pool.work(counter::incrementAndGet);
        }

        Thread.sleep(1000);
        pool.shutdown();

        assertThat(counter.get()).isEqualTo(taskCount);
    }

    @Test
    void whenShutdownThenNoMoreTasksProcessed() throws InterruptedException {
        ThreadPool pool = new ThreadPool();
        AtomicInteger counter = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {
            pool.work(counter::incrementAndGet);
        }

        Thread.sleep(500);
        pool.shutdown();

        for (int i = 0; i < 5; i++) {
            pool.work(counter::incrementAndGet);
        }

        Thread.sleep(500);
        assertThat(counter.get()).isEqualTo(5);
    }
}

package ru.job4j.concurrent;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CASCountTest {

    @Test
    public void whenIncrementFromMultipleThreadsThenCorrectValue() throws InterruptedException {
        CASCount count = new CASCount();
        int threads = 10;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(threads);

        Runnable task = () -> {
            for (int i = 0; i < incrementsPerThread; i++) {
                count.increment();
            }
            latch.countDown();
        };

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            executor.execute(task);
        }

        latch.await();
        executor.shutdown();

        assertThat(count.get()).isEqualTo(threads * incrementsPerThread);
    }

    @Test
    public void whenSingleThreadIncrementThenCorrectValue() {
        CASCount count = new CASCount();
        count.increment();
        count.increment();
        count.increment();

        assertThat(count.get()).isEqualTo(3);
    }
}

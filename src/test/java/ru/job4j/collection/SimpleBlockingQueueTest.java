package ru.job4j.collection;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class SimpleBlockingQueueTest {
    @Test
    public void whenFetchAllThenGetIt() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            IntStream.range(0, 5).forEach(i -> {
                try {
                    queue.offer(i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });

        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );

        producer.start();
        consumer.start();

        producer.join();
        consumer.interrupt();
        consumer.join();

        assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
    }

    @Test
    public void whenMultipleProducersAndConsumersThenCorrectOrder() throws InterruptedException {
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);

        Thread producer1 = new Thread(() -> {
            IntStream.range(0, 5).forEach(i -> {
                try {
                    queue.offer(i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });

        Thread producer2 = new Thread(() -> {
            IntStream.range(5, 10).forEach(i -> {
                try {
                    queue.offer(i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });

        Thread consumer1 = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer2 = new Thread(() -> {
            while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                try {
                    buffer.add(queue.poll());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        producer1.join();
        producer2.join();
        consumer1.interrupt();
        consumer2.interrupt();
        consumer1.join();
        consumer2.join();

        assertThat(buffer).containsExactlyInAnyOrderElementsOf(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    }
}
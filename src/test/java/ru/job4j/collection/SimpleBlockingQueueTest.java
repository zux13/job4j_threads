package ru.job4j.collection;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleBlockingQueueTest {

    @Test
    void testProducerConsumer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);

        Thread producer = new Thread(() -> {
            try {
                queue.offer(1);
                queue.offer(2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(50);
                assertEquals(1, queue.poll());
                assertEquals(2, queue.poll());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    @Test
    void testMultipleProducersAndConsumers() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);

        Thread producer1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                queue.offer(i);
            }
        });

        Thread producer2 = new Thread(() -> {
            for (int i = 3; i < 6; i++) {
                queue.offer(i);
            }
        });

        Thread consumer1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                assertNotNull(queue.poll());
            }
        });

        Thread consumer2 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                assertNotNull(queue.poll());
            }
        });

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        producer1.join();
        producer2.join();
        consumer1.join();
        consumer2.join();
    }
}

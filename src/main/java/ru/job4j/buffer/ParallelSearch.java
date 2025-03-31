package ru.job4j.buffer;

import ru.job4j.collection.SimpleBlockingQueue;

public class ParallelSearch {

    private static volatile boolean finished = false;

    public static void main(String[] args) {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);

        final Thread consumer = new Thread(
                () -> {
                    while (!finished) {
                        try {
                            System.out.println(queue.poll());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );

        consumer.start();

        Thread producer = new Thread(
                () -> {
                    for (int index = 0; index != 3; index++) {
                        try {
                            queue.offer(index);
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    finished = true;
                }
        );

        producer.start();

        try {
            producer.join();
            consumer.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

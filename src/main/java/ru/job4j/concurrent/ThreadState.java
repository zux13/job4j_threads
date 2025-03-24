package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );

        printState(first);
        first.start();
        printState(second);
        second.start();

        while (first.getState() != Thread.State.TERMINATED
                || second.getState() != Thread.State.TERMINATED) {
            printState(first);
            printState(second);
        }

        printState(first);
        printState(second);

        System.out.println("Job completed");
    }

    private static void printState(Thread thread) {
        System.out.println(thread.getName() + ": " + thread.getState());
    }
}
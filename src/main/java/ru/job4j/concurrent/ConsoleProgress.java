package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        var process = new char[] {'—', '\\', '|', '/'};
        int currentIndex = 0;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(500);
                currentIndex = (currentIndex > 3) ? 0 : currentIndex;
                System.out.print("\rload: " + process[currentIndex++]);
            }
        } catch (InterruptedException e) {
            System.out.print("\rprocess interrupted!");
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        Thread progress = new Thread(new ConsoleProgress());

        progress.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        progress.interrupt();

    }
}

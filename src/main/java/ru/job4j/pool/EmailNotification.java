package ru.job4j.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EmailNotification {
    private final ExecutorService pool;

    public EmailNotification() {
        pool = Executors.newCachedThreadPool();
    }

    public void emailTo(User user) {
        pool.submit(() -> sendEmail(user));
    }

    public void send(String subject, String body, String email) {
        System.out.printf("Sending email to %s%nSubject: %s%nBody:%s%n", email, subject, body);
    }

    public void close() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sendEmail(User user) {
        String body = String.format("Add a new event to %s", user.username());
        String subject = String.format("Notification %s to email %s", user.username(), user.email());
        send(subject, body, user.email());
    }
}

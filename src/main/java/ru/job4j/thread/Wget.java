package ru.job4j.thread;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Wget implements Runnable {
    private static final int BUFFER_SIZE = 1024;
    private static final int NANO_TO_MILLIS = 1_000_000;
    private final String url;
    private final int speed;
    private final String fileName;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
        this.fileName = extractFileName(url);
    }

    @Override
    public void run() {
        var file = new File(fileName);
        try (InputStream input = new URL(url).openStream();
             OutputStream output = new FileOutputStream(file)) {
            byte[] dataBuffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                long startTime = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                long elapsedTime = System.nanoTime() - startTime;

                long expectedTime = bytesRead / speed;
                long sleepTime = expectedTime - (elapsedTime / NANO_TO_MILLIS);
                if (sleepTime > 0) {
                    System.out.println("Засыпаем на: " + sleepTime + " мс.");
                    Thread.sleep(sleepTime);
                }
            }
            System.out.println("Файл успешно загружен: " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Поток был прерван.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (!validateArgs(args)) {
            System.exit(1);
        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);

        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }

    private static boolean validateArgs(String[] args) {
        if (args.length != 2) {
            System.err.println("Ошибка: необходимо передать 2 аргумента (URL и число).");
            return false;
        }

        if (!isValidURL(args[0])) {
            System.err.println("Ошибка: первый аргумент должен быть валидным URL.");
            return false;
        }

        if (!isValidInt(args[1]) || Integer.parseInt(args[1]) <= 0) {
            System.err.println("Ошибка: скорость должна быть целым числом больше 0.");
            return false;
        }

        return true;
    }

    private static boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    private static boolean isValidInt(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String extractFileName(String urlString) {
        String prefix = "." + File.separator + "data" + File.separator;
        File dataDir = new File(prefix);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        try {
            URL url = new URL(urlString);
            String path = url.getPath();
            String fileName = new File(path).getName();
            return fileName.isEmpty() ? prefix + "downloaded_file" : prefix + fileName;
        } catch (MalformedURLException e) {
            return prefix + "downloaded_file";
        }
    }

}
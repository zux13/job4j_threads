package ru.job4j.thread;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Wget implements Runnable {
    private static final int MILLIS_IN_SECOND = 1000;
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
            byte[] dataBuffer = new byte[speed];
            int bytesRead, bytesCount = 0;
            long startMillis = System.currentTimeMillis();
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                output.write(dataBuffer, 0, bytesRead);
                bytesCount += bytesRead;
                if (bytesCount < speed) {
                    continue;
                }
                bytesCount = 0;
                long sleepTime = MILLIS_IN_SECOND - (System.currentTimeMillis() - startMillis);
                if (sleepTime > 0) {
                    System.out.printf("Засыпаем на %d мс.\n", sleepTime);
                    Thread.sleep(sleepTime);
                }
                startMillis = System.currentTimeMillis();
            }
            System.out.printf("Файл '%s' успешно загружен.\n", fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Поток был прерван.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (!validateArgs(args)) {
            throw new RuntimeException("Invalid arguments");
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
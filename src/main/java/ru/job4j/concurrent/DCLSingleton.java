package ru.job4j.concurrent;

public final class DCLSingleton {

    private volatile static DCLSingleton instance;
    /* В данном случае ключевое слово `volatile` гарантирует атомарность записи в переменную `instance`,
    * что позволяет избежать состояния частичной инициализации или чтения её значения из кэша процессора */

    public static DCLSingleton getInstance() {
        if (instance == null) {
            synchronized (DCLSingleton.class) {
                if (instance == null) {
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }

    private DCLSingleton() {
    }

}
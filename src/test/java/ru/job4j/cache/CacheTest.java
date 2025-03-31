package ru.job4j.cache;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CacheTest {
    @Test
    public void whenAddFind() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base");
    }

    @Test
    public void whenAddUpdateFind() throws OptimisticException {
        var base = new Base(1, "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(new Base(1, "Base updated", 1));
        var find = cache.findById(base.id());
        assertThat(find.get().name())
                .isEqualTo("Base updated");
    }

    @Test
    public void whenAddDeleteFind() throws OptimisticException {
        var base = new Base(1,   "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.delete(1);
        var find = cache.findById(base.id());
        assertThat(find.isEmpty()).isTrue();
    }

    @Test
    public void whenMultiUpdateThrowException() throws OptimisticException {
        var base = new Base(1,  "Base", 1);
        var cache = new Cache();
        cache.add(base);
        cache.update(base);
        assertThatThrownBy(() -> cache.update(base))
                .isInstanceOf(OptimisticException.class);
    }

    @Test
    public void whenAddFromMultipleThreadsThenCacheHasAllElements() throws InterruptedException {
        var cache = new Cache();
        var base1 = new Base(1, "Base 1", 1);
        var base2 = new Base(2, "Base 2", 1);

        Thread thread1 = new Thread(() -> {
            try {
                cache.add(base1);
            } catch (OptimisticException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                cache.add(base2);
            } catch (OptimisticException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertThat(cache.findById(1).get().name()).isEqualTo("Base 1");
        assertThat(cache.findById(2).get().name()).isEqualTo("Base 2");
    }

    @Test
    public void whenUpdateFromMultipleThreadsThenThrowOptimisticException() throws InterruptedException {
        var cache = new Cache();
        var base = new Base(1, "Base", 1);

        cache.add(base);

        Thread thread1 = new Thread(() -> {
            try {
                cache.update(new Base(1, "Base updated", 1));
            } catch (OptimisticException e) {
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                cache.update(new Base(1, "Base updated again", 1));
            } catch (OptimisticException e) {
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertThatThrownBy(() -> cache.update(new Base(1, "Base updated once more", 1)))
                .isInstanceOf(OptimisticException.class);
    }
}
package ru.job4j.pool;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ParallelSearchTest {

    @Test
    void whenSearchIntegerThenFound() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int index = ParallelSearch.search(array, 5);
        assertThat(index).isEqualTo(4);
    }

    @Test
    void whenSearchStringThenFound() {
        String[] array = {"apple", "banana", "cherry", "date", "fig"};
        int index = ParallelSearch.search(array, "cherry");
        assertThat(index).isEqualTo(2);
    }

    @Test
    void whenSearchNotFoundThenMinusOne() {
        Integer[] array = {1, 2, 3, 4, 5};
        int index = ParallelSearch.search(array, 10);
        assertThat(index).isEqualTo(-1);
    }

    @Test
    void whenSmallArrayThenLinearSearch() {
        Integer[] array = {10, 20, 30, 40, 50};
        int index = ParallelSearch.search(array, 30);
        assertThat(index).isEqualTo(2);
    }

    @Test
    void whenLargeArrayThenRecursiveSearch() {
        Integer[] array = new Integer[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = i * 2;
        }
        int index = ParallelSearch.search(array, 86);
        assertThat(index).isEqualTo(43);
    }

    @Test
    void whenEmptyArrayThenMinusOne() {
        Integer[] array = new Integer[0];
        int index = ParallelSearch.search(array, 10);
        assertThat(index).isEqualTo(-1);
    }
}

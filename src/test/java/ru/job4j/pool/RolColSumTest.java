package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolColSumTest {

    @Test
    void when3x3MatrixThenCorrectSums() {
        int[][] matrix = {
                {1, 2, 5},
                {2, 10, 11},
                {14, 8, 9}
        };

        Sums[] expected = {
                new Sums(8, 17),
                new Sums(23, 20),
                new Sums(31, 25)
        };

        Sums[] actual = RolColSum.sum(matrix);

        assertArrayEquals(actual, expected);
    }

    @Test
    void whenAsync3x3MatrixThenSameAsSync() {
        int[][] matrix = {
                {1, 2, 5},
                {2, 10, 11},
                {14, 8, 9}
        };

        Sums[] syncSums = RolColSum.sum(matrix);
        Sums[] asyncSums = RolColSum.asyncSum(matrix);

        assertArrayEquals(syncSums, asyncSums);
    }

    @Test
    void when1x1MatrixThenCorrectSums() {
        int[][] matrix = {{42}};

        Sums[] expected = {new Sums(42, 42)};
        Sums[] actual = RolColSum.asyncSum(matrix);

        assertArrayEquals(expected, actual);
    }

    @Test
    void whenEmptyMatrixThenEmptyResult() {
        int[][] matrix = new int[0][0];

        Sums[] sums = RolColSum.sum(matrix);

        assertEquals(0, sums.length);
    }
}

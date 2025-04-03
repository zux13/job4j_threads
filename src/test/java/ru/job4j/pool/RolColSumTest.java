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

        RolColSum.Sums[] sums = RolColSum.sum(matrix);

        assertArrayEquals(new int[]{8, 23, 31}, new int[]{sums[0].getRowSum(), sums[1].getRowSum(), sums[2].getRowSum()});
        assertArrayEquals(new int[]{17, 20, 25}, new int[]{sums[0].getColSum(), sums[1].getColSum(), sums[2].getColSum()});
    }

    @Test
    void whenAsync3x3MatrixThenSameAsSync() {
        int[][] matrix = {
                {1, 2, 5},
                {2, 10, 11},
                {14, 8, 9}
        };

        RolColSum.Sums[] syncSums = RolColSum.sum(matrix);
        RolColSum.Sums[] asyncSums = RolColSum.asyncSum(matrix);

        for (int i = 0; i < matrix.length; i++) {
            assertEquals(syncSums[i].getRowSum(), asyncSums[i].getRowSum());
            assertEquals(syncSums[i].getColSum(), asyncSums[i].getColSum());
        }
    }

    @Test
    void when1x1MatrixThenCorrectSums() {
        int[][] matrix = {{42}};

        RolColSum.Sums[] sums = RolColSum.sum(matrix);

        assertEquals(42, sums[0].getRowSum());
        assertEquals(42, sums[0].getColSum());
    }

    @Test
    void whenEmptyMatrixThenEmptyResult() {
        int[][] matrix = new int[0][0];

        RolColSum.Sums[] sums = RolColSum.sum(matrix);

        assertEquals(0, sums.length);
    }
}

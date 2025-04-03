package ru.job4j.pool;

import java.util.concurrent.CompletableFuture;

public class RolColSum {

    public static class Sums {
        private int rowSum;
        private int colSum;

        public Sums(int rowSum, int colSum) {
            this.rowSum = rowSum;
            this.colSum = colSum;
        }

        public int getRowSum() {
            return rowSum;
        }

        public void setRowSum(int rowSum) {
            this.rowSum = rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        public void setColSum(int colSum) {
            this.colSum = colSum;
        }
    }

    public static Sums[] sum(int[][] matrix) {
        int len = matrix.length;
        Sums[] result = new Sums[len];

        for (int i = 0; i < len; i++) {
            int col = 0;
            int row = 0;
            for (int j = 0; j < len; j++) {
                row += matrix[i][j];
                col += matrix[j][i];
            }
            result[i] = new Sums(row, col);
        }

        return result;
    }

    public static Sums[] asyncSum(int[][] matrix) {
        int len = matrix.length;
        Sums[] result = new Sums[len];

        CompletableFuture<Integer>[] rowFutures = new CompletableFuture[len];
        CompletableFuture<Integer>[] colFutures = new CompletableFuture[len];

        for (int i = 0; i < len; i++) {
            rowFutures[i] = rowSum(matrix, i);
            colFutures[i] = colSum(matrix, i);
        }

        CompletableFuture.allOf(rowFutures).join();
        CompletableFuture.allOf(colFutures).join();

        for (int i = 0; i < len; i++) {
            result[i] = new Sums(rowFutures[i].join(), colFutures[i].join());
        }

        return result;
    }

    public static CompletableFuture<Integer> rowSum(int[][] matrix, int rowIndex) {
        return CompletableFuture.supplyAsync(() -> {
            int result = 0;
            for (int i = 0; i < matrix.length; i++) {
                result += matrix[rowIndex][i];
            }
            return result;
        });
    }

    public static CompletableFuture<Integer> colSum(int[][] matrix, int colIndex) {
        return CompletableFuture.supplyAsync(() -> {
            int result = 0;
            for (int[] row : matrix) {
                result += row[colIndex];
            }
            return result;
        });
    }

}
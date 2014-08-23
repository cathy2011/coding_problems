import java.util.Scanner;

public class MaximumSum {

  private static class Computer {

    private int[][] matrix; // Column-wise sum.
    private int result;

    public Computer readInput() {
      try (Scanner in = new Scanner(System.in);) {
        int dim = in.nextInt();
        matrix = new int[dim][dim];
        for (int i = 0; i < dim; ++i) {
          for (int j = 0; j < dim; ++j) {
            if (i > 0) {
              matrix[i][j] = matrix[i - 1][j] + in.nextInt();
            } else {
              matrix[i][j] = in.nextInt();
            }
          }
        }
      }
      return this;
    }

    public Computer compute() {
      result = Integer.MIN_VALUE;
      for (int i = 0; i < matrix.length; ++i) {
        for (int j = i; j < matrix.length; ++j) {
          // from row #i to row #j, inclusive.
          int[] tmp = new int[matrix.length];
          for (int k = 0; k < matrix.length; ++k) {
            tmp[k] = matrix[j][k] - (i == 0 ? 0 : matrix[i - 1][k]);
          }
          // One-dimension DP
          result = Math.max(tmp[0], result);
          for (int k = 1; k < tmp.length; ++k) {
            tmp[k] = tmp[k - 1] > 0 ? tmp[k - 1] + tmp[k] : tmp[k];
            if (tmp[k] > result) {
              result = tmp[k];
            }
          }
        }
      }
      return this;
    }

    public void outputResult() {
      System.out.println(result);
    }

  }

  public static void main(String[] args) {
    new Computer().readInput().compute().outputResult();
  }

}

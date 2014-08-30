import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

// 14119199 125 Numbering Paths Accepted  JAVA  0.682 2014-08-30 15:37:03

public class NumberingPaths {
  
  private static class Input {
    
    private int max = Integer.MIN_VALUE;
    private boolean[][] matrix = new boolean[30][30];
    
    private Input() {
      for (int i = 0; i < 30; ++i) {
        Arrays.fill(matrix[i], false);
      }
    }
    
    public int getMax() {
      return max + 1;
    }
    
    public boolean isConnected(int from, int to) {
      return matrix[from][to];
    }
    
    public static Iterator<Input> getIterator() {
      return new Iterator<Input>() {

        private Scanner in = new Scanner(System.in);
        
        @Override
        public boolean hasNext() {
          if (in.hasNext()) { return true; }
          in.close();
          return false;
        }

        @Override
        public Input next() {
          Input input = new Input();
          int numOfStreets = in.nextInt();
          for (int i = 0; i < numOfStreets; ++i) {
            int from = in.nextInt();
            int to = in.nextInt();
            input.matrix[from][to] = true;
            if (from > input.max) { input.max = from; }
            if (to > input.max) { input.max = to; }
          }
          return input;
        }
        
        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
        
      };
    }
  }
  
  private static int[][] find(Input input) {
    final int n = input.getMax();  // number of nodes.
    // paths[k][i][j]: the number of unique at-most-k-hop paths from i to j. -1 means infinity.
    int[][][] paths = new int[2 * n + 1][n][n];
    // All possible nodes for paths[k][i][j], excluding i and j.
    // Very important for detecting loops.
    Object[][][] nodes = new Object[2 * n + 1][n][n];
    // paths[0][i][j] = 0 for all i, j.
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        paths[0][i][j] = 0;
        nodes[0][i][j] = (Object) new HashSet<Integer>();
      }
    }
    for (int k = 1; k < 2 * n + 1; ++k) {
      for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {  // compute paths[k][i][j].
          int sum = input.isConnected(i, j) ? 1 : 0;
          HashSet<Integer> tmp = new HashSet<Integer>();
          for (int via = 0; via < n; ++via) {
            if (paths[k - 1][i][via] == 0 || !input.isConnected(via, j)) {
              continue;
            }
            @SuppressWarnings("unchecked")
            HashSet<Integer> existing = (HashSet<Integer>) nodes[k - 1][i][via];
            if (i == j || paths[k - 1][i][via] == -1 || existing.contains(j)) {
              // A loop may occur.
              sum = -1;
              break;
            }
            sum += paths[k - 1][i][via];
            tmp.addAll(existing);
            tmp.add(via);
          }
          paths[k][i][j] = sum;
          nodes[k][i][j] = tmp;
        }  
      }
    }
    return paths[2 * n];
  }
  
  private static void output(int seq, int[][] result) {
    System.out.println("matrix for city " + seq);
    for (int i = 0; i < result.length; ++i) {
      for (int j = 0; j < result[i].length; ++j) {
        System.out.print((j == 0 ? "" : " ") + result[i][j]); 
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    int seq = 0;
    Iterator<Input> it = Input.getIterator();
    while (it.hasNext()) {
      output(seq++, find(it.next()));
    }
  }

}

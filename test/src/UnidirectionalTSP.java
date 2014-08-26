// 14095834 116 Unidirectional TSP Accepted JAVA 2.855  2014-08-26 01:51:28 
// 14095687 116 Unidirectional TSP Wrong answer JAVA 1.829  2014-08-26 00:21:46
//    - Used greedy to compute paths.
// 14095679 116 Unidirectional TSP Compilation error  JAVA 0.000  2014-08-26 00:19:38
//    - remove() was forgot for Iterator.


import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 116 - Unidirectional TSP
 *
 * @author QQ
 *
 */
public class UnidirectionalTSP {

  // Used to iterate input data sets.
  private static class InputIterator implements Iterator<int[][]>, AutoCloseable {

    Scanner in = null;

    public InputIterator() {
      in = new Scanner(System.in);
    }

    @Override
    public boolean hasNext() {
      return in.hasNextInt();
    }

    @Override
    public int[][] next() {
      int numOfRows = in.nextInt();
      int numOfColumns = in.nextInt();
      int[][] matrix = new int[numOfRows][numOfColumns];
      for (int i = 0; i < numOfRows; ++i) {
        for (int j = 0; j < numOfColumns; ++j) {
          matrix[i][j] = in.nextInt();
        }
      }
      return matrix;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws Exception {
      in.close();
    }

  }

  private static class RowIndexSorter implements Comparator<Integer> {

    private int[][] matrix;
    private int column;
    private Object[][] prev;

    public RowIndexSorter(int[][] matrix, Object[][] prev, int column) {
      this.matrix = matrix;
      this.prev = prev;
      this.column = column;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Integer o1, Integer o2) {
      if (matrix[o1][column] == matrix[o2][column]) {
        return new PathComparator().compare((List<Integer>) prev[o1][column],
            (List<Integer>) prev[o2][column]);
      }
      return matrix[o1][column] - matrix[o2][column];
    }

  }

  private static class PathComparator implements Comparator<List<Integer>> {

    @Override
    public int compare(List<Integer> arg0, List<Integer> arg1) {
      if (arg0 == null || arg1 == null || arg0.size() != arg1.size()) {
        return 0;
      }
      for (int i = 0; i < arg0.size(); ++i) {
        if (arg0.get(i) != arg1.get(i)) {
          return arg0.get(i) - arg1.get(i);
        }
      }
      return 0;
    }

  }

  private static String pathToString(List<Integer> path) {
    StringBuilder builder = new StringBuilder();
    boolean isFirst = true;
    for (Integer p : path) {
      if (isFirst) {
        isFirst = false;
      } else {
        builder.append(' ');
      }
      builder.append(p + 1);
    }
    return builder.toString();
  }

  // Use DP.
  //
  // In particular, we maintain a 2-D array m[][] with m[i][j] defined as
  // the minimal weight of paths from column #0 to #j ending at cell (i, j).
  // Then we have:
  // m[i][0] = weightMatrix[i][0], for each valid row index i.
  //
  // m[i][j] = min(m[i + 1][j - 1], m[i][j - 1], m[i - 1][j - 1]) +
  // weightMatrix[i][j]
  // for each valid column index j > 0, starting from 1. Use mod operation
  // whenever necessary.
  //
  // Output the answer as min(m[i][last column]) for each valid row index i.
  private static StringBuilder findMinimumWeight(int[][] weightMatrix) {
    final int numOfRows = weightMatrix.length;
    final int numOfColumns = weightMatrix[0].length;
    int[][] m = new int[numOfRows][numOfColumns];
    Object[][] paths = new Object[numOfRows][numOfColumns];

    // Start from column #0.
    for (int i = 0; i < numOfRows; ++i) {
      m[i][0] = weightMatrix[i][0];
      List<Integer> tmp = new LinkedList<Integer>();
      tmp.add(i);
      paths[i][0] = tmp;
    }

    // Compute other columns starting from column 1.
    for (int j = 1; j < numOfColumns; ++j) { // for column #j
      for (int i = 0; i < numOfRows; ++i) {
        m[i][j] = weightMatrix[i][j];
        Integer[] prevRows = { (i + 1) % numOfRows, i, (i - 1 + numOfRows) % numOfRows };
        Arrays.sort(prevRows, new RowIndexSorter(m, paths, j - 1));
        m[i][j] += m[prevRows[0]][j - 1];
        List<Integer> tmp = new LinkedList<Integer>();
        tmp.addAll((List<Integer>) paths[prevRows[0]][j - 1]);
        tmp.add(i);
        paths[i][j] = tmp;
      }
    }

    // Find the answer.
    int minWeight = Integer.MAX_VALUE;
    List<Integer> minPath = null;
    PathComparator comp = new PathComparator();
    for (int i = 0; i < numOfRows; ++i) {
      if (m[i][numOfColumns - 1] < minWeight) {
        minWeight = m[i][numOfColumns - 1];
        minPath = (List<Integer>) paths[i][numOfColumns - 1];
      } else if (m[i][numOfColumns - 1] == minWeight
          && comp.compare((List<Integer>) paths[i][numOfColumns - 1], minPath) < 0) {
        minPath = (List<Integer>) paths[i][numOfColumns - 1];
      }
    }

    // Compose output.
    return new StringBuilder().append(pathToString(minPath)).append('\n').append(minWeight);

  }

  public static void main(String[] args) {
    try (InputIterator it = new InputIterator();) {
      while (it.hasNext()) {
        System.out.println(findMinimumWeight(it.next()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}

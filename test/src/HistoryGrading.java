import java.util.Comparator;
import java.util.Scanner;

public class HistoryGrading {

  private static class Reference implements Comparator<Integer> {

    private int[][] matrix;

    public Reference(int[] reference) {
      matrix = new int[reference.length][reference.length];
      for (int i = 0; i < reference.length - 1; ++i) {
        matrix[reference[i]][reference[i]] = 0;
        for (int j = i + 1; j < reference.length; ++j) {
          matrix[reference[i]][reference[j]] = -1; // i precedes j.
          matrix[reference[j]][reference[i]] = 1;
        }
      }
    }

    @Override
    public int compare(Integer arg0, Integer arg1) {
      return matrix[arg0][arg1];
    }

  }

  private static class Grader {

    Comparator<Integer> reference;

    public Grader(int[] reference) {
      this.reference = new Reference(reference);
    }

    public int grade(int[] rank) {
      // m[i]: longest sequence in the right order ending at and including
      // rank[i].
      int[] m = new int[rank.length];
      m[0] = 1;
      int max = m[0];
      for (int i = 1; i < m.length; ++i) {
        m[i] = 1; // itself only.
        for (int k = 0; k < i; ++k) {
          if (reference.compare(rank[k], rank[i]) == -1 && m[k] + 1 > m[i]) {
            // rank[i] can be appended to m[k].
            m[i] = m[k] + 1;
          }
        }
        if (m[i] > max) {
          max = m[i];
        }
      }
      return max;
    }
  }

  private static int[] rankToOrder(int[] rank) {
    int[] order = new int[rank.length];
    for (int i = 0; i < rank.length; ++i) {
      order[rank[i]] = i;
    }
    return order;
  }

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in);) {
      int numOfEvents = in.nextInt();
      int[] reference = new int[numOfEvents];
      for (int i = 0; i < numOfEvents; ++i) {
        reference[i] = in.nextInt() - 1;
      }
      Grader grader = new Grader(rankToOrder(reference));
      while (in.hasNext()) {
        int[] rank = new int[numOfEvents];
        for (int i = 0; i < numOfEvents; ++i) {
          rank[i] = in.nextInt() - 1;
        }
        System.out.println(grader.grade(rankToOrder(rank)));
      }
    }
  }

}

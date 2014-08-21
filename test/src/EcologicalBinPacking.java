import java.util.Iterator;
import java.util.Scanner;

class Input {
  public static final int SIZE = 3; // 3 bins, 3 colors.
  public static final String COLORS = "BGC"; // 0:B, 1:G, 2:C

  private int[][] bins;

  public Input(int[][] bins) {
    this.bins = bins;
  }

  public int[][] getBins() {
    return bins;
  }

  public int getSum() {
    int sum = 0;
    for (int[] colors : bins) {
      for (int color : colors) {
        sum += color;
      }
    }
    return sum;
  }

  public BinPacker getBinPacker() {
    return new BinPacker(this);
  }
}

class BinPacker {

  // All 6 possible color assignments.
  private static final int ASSIGNMENTS[][] = { { 0, 2, 1 }, // BCG
      { 0, 1, 2 }, // BGC
      { 2, 0, 1 }, // CBG
      { 2, 1, 0 }, // CGB
      { 1, 0, 2 }, // GBC
      { 1, 2, 0 }  // GCB
  };

  private Input input;

  public BinPacker(Input input) {
    this.input = input;
  }

  public ResultPrinter getResultPrinter() {
    int minMovements = Integer.MAX_VALUE;
    int[] assignment = null;
    for (int[] assign : ASSIGNMENTS) {
      int tmp = getMinMovementsForAssignment(assign);
      if (tmp < minMovements) {
        minMovements = tmp;
        assignment = assign;
      }
    }
    return new ResultPrinter(toAssignmentString(assignment), minMovements);
  }

  private int getMinMovementsForAssignment(int[] assignment) {
    int sum = input.getSum();
    for (int i = 0; i < assignment.length; ++i) {
      sum -= input.getBins()[i][assignment[i]];
    }
    return sum;
  }

  private String toAssignmentString(int[] assignment) {
    StringBuilder builder = new StringBuilder();
    for (int colorInt : assignment) {
      builder.append(Input.COLORS.charAt(colorInt));
    }
    return builder.toString();
  }
}

class ResultPrinter {

  private String assignment;
  private int minMovements;

  public ResultPrinter(String assignment, int minMovements) {
    this.assignment = assignment;
    this.minMovements = minMovements;
  }

  public void output() {
    System.out.println(assignment + " " + minMovements);
  }
}

class Inputs implements Iterable<Input> {

  private Scanner is;

  public Inputs(Scanner is) {
    this.is = is;
  }

  @Override
  public Iterator<Input> iterator() {
    return new Iterator<Input>() {

      @Override
      public boolean hasNext() {
        return is.hasNext();
      }

      @Override
      public Input next() {
        int[][] bins = new int[Input.SIZE][Input.SIZE];
        for (int i = 0; i < Input.SIZE; ++i) {
          for (int j = 0; j < Input.SIZE; ++j) {
            bins[i][j] = is.nextInt();
          }
        }
        return new Input(bins);
      }
      
      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }

    };
  }

}

public class EcologicalBinPacking {

  public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in);) {
      for (Input input : new Inputs(sc)) {
        input.getBinPacker().getResultPrinter().output();
      }
    }
  }

}

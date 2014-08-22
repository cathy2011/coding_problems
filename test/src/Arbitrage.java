import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class ExchangeRateInfo {

  private double rate;
  private List<Integer> path;

  public ExchangeRateInfo(double rate) {
    this.rate = rate;
    path = new LinkedList<Integer>();
  }

  public double getRate() {
    return rate;
  }

  public List<Integer> getPath() {
    return path;
  }

  public void update(double rate, List<Integer> path) {
    if (rate > this.rate) {
      this.rate = rate;
      this.path = path;
    }
  }

}

class ArbitrageInput {

  private double[][] rateMatrix;
  private int n;

  public ArbitrageInput(double[][] rateMatrix) {
    this.rateMatrix = rateMatrix;
    n = rateMatrix.length;
  }

  public ArbitrageResult compute() {
    // [i][j][k]: max from currency j to k with at most i intermediate hops.
    ExchangeRateInfo[][][] rates = new ExchangeRateInfo[n][n][n];
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        rates[0][i][j] = new ExchangeRateInfo(rateMatrix[i][j]);
      }
    }

    // Stop when we have rates[i][i] > 1.0 for some i.
    List<Integer> path = new LinkedList<Integer>();
    for (int len = 1; len < n; ++len) {
      for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
          // Compute rates[len][i][j].
          rates[len][i][j] = new ExchangeRateInfo(-1.0);
          for (int k = 0; k < n; ++k) {
            List<Integer> tmpList = new LinkedList<Integer>();
            tmpList.addAll(rates[len - 1][i][k].getPath());
            tmpList.add(k);
            rates[len][i][j].update(rates[len - 1][i][k].getRate() * rateMatrix[k][j], tmpList);
          }

          if (i == j && rates[len][i][j].getRate() > 1.01) {
            path.add(i);
            path.addAll(rates[len][i][j].getPath());
            path.add(j);
            return new ArbitrageResult(path);
          }
        }
      }
    }
    return new ArbitrageResult(path);
  }

}

class ArbitrageResult {
  private static final String NO_ARBITRAGE = "no arbitrage sequence exists";

  private List<Integer> path;

  public ArbitrageResult(List<Integer> path) {
    this.path = path;
  }

  public void output() {
    if (path.isEmpty()) {
      System.out.println(NO_ARBITRAGE);
    } else {
      StringBuilder builder = new StringBuilder();
      Iterator<Integer> it = path.iterator();
      builder.append(it.next() + 1);
      while (it.hasNext()) {
        builder.append(" ").append(it.next() + 1);
      }
      System.out.println(builder);
    }
  }
}

class ArbitrageInputs implements Iterable<ArbitrageInput> {

  private class InputsIterator implements Iterator<ArbitrageInput> {

    @Override
    public boolean hasNext() {
      return in.hasNext();
    }

    @Override
    public ArbitrageInput next() {
      int numCurrencies = in.nextInt();
      double[][] matrix = new double[numCurrencies][numCurrencies];
      for (int i = 0; i < numCurrencies; ++i) {
        for (int j = 0; j < numCurrencies; ++j) {
          matrix[i][j] = j == i ? 1.0 : in.nextDouble();
        }
      }
      return new ArbitrageInput(matrix);
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private Scanner in;

  public ArbitrageInputs(Scanner in) {
    this.in = in;
  }

  @Override
  public Iterator<ArbitrageInput> iterator() {
    return new InputsIterator();
  }
}

public class Arbitrage {

  public static void main(String[] args) {
    try (Scanner sc = new Scanner(System.in);) {
      for (ArbitrageInput input : new ArbitrageInputs(sc)) {
        input.compute().output();
      }
    }
  }

}

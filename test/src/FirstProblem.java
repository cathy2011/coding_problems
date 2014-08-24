// The two numbers could come in an arbitrary order.

import java.util.Iterator;
import java.util.Scanner;

//Interface for a single piece of work.
interface Work {

  // Perform this piece of work and optionally return the next piece
  // if any.
  public Work perform();

}

class InputDataSets implements Iterator<Work>, Iterable<Work> {

  Scanner input;

  public InputDataSets(Scanner input) {
    this.input = input;
  }

  @Override
  public boolean hasNext() {
    return input.hasNext();
  }

  @Override
  public Iterator<Work> iterator() {
    return this;
  }
  
  public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Work next() {
    return new FindLongestCycle(input.nextInt(), input.nextInt());
  }

}

class FindLongestCycle implements Work {

  private Integer end;
  private Integer start;

  public FindLongestCycle(Integer start, Integer end) {
    this.start = start;
    this.end = end;
  }

  private int getCycleLen(int n) {
    int len = 0;
    while (true) {
      ++len;
      if (n == 1) {
        break;
      }
      n = n % 2 == 0 ? n / 2 : 3 * n + 1;
    }
    return len;
  }

  @Override
  public Work perform() {
    int from = Math.min(start, end);
    int to = Math.max(start, end);
    int longest = Integer.MIN_VALUE;
    for (int i = from; i <= to; ++i) {
      int tmp = getCycleLen(i);
      if (tmp > longest) {
        longest = tmp;
      }
    }
    return new OutputResult(start, end, longest);
  }

}

class OutputResult implements Work {

  private Integer end;
  private Integer result;
  private Integer start;

  public OutputResult(Integer start, Integer end, Integer result) {
    this.start = start;
    this.end = end;
    this.result = result;
  }

  @Override
  public Work perform() {
    System.out.println(start + " " + end + " " + result);
    return null;
  }

}

public class FirstProblem {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    try {
      for (Work work : new InputDataSets(sc)) {
        while (work != null) work = work.perform();
      }
    } finally {
      sc.close();
    }
  }

}

import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;

public class CatInHat {

  private static class Inputs implements AutoCloseable, Iterable<Input> {

    private Scanner in = null;

    public Inputs(InputStream is) {
      this.in = new Scanner(is);
    }

    @Override
    public void close() throws Exception {
      in.close();
    }

    @Override
    public Iterator<Input> iterator() {
      return new Iterator<Input>() {

        private Input next = null;

        @Override
        public boolean hasNext() {
          int height = in.nextInt();
          int numOfWorkers = in.nextInt();
          if (height == 0 && numOfWorkers == 0) {
            return false;
          }
          next = new Input(height, numOfWorkers);
          return true;
        }

        @Override
        public Input next() {
          return this.next;
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }

  }

  private static class Input {

    private Integer initialHeight;
    private Integer numOfWorkers;

    public Input(Integer initialHeight, Integer numOfWorkers) {
      this.initialHeight = initialHeight;
      this.numOfWorkers = numOfWorkers;
    }

    public Integer getInitialHeight() {
      return initialHeight;
    }

    public Integer getNumOfWorkers() {
      return numOfWorkers;
    }

    public Computer getComputer() {
      return new Computer(this);
    }
  }

  private static class Computer {

    private Input input;

    public Computer(Input input) {
      this.input = input;
    }

    public Outputer compute() {
      if (input.getInitialHeight() == 1 && input.getNumOfWorkers() == 1) {
        return new Outputer(0, 1);
      }

      // compute n and levels.
      long n = -1;
      long level = 1;
      while (true) {
        double estimated = Math.pow(input.getNumOfWorkers(), 1.0 / (double) level);
        if (Math.abs(Math.pow(input.getInitialHeight(), 1.0 / (double) level) - estimated - 1) <= 1E-6) {
          // Fount it.
          n = Math.round(estimated);
          break;
        }
        ++level;
      }
      
      // compute results.
      long numOfNonWorkingCats = 0;
      for (int i = 0; i < level; ++i) {
        numOfNonWorkingCats += Math.round(Math.pow(n, i));
      }
      
      long sumOfAll = 0;
      for (int i = 0; i <= level; ++i) {
        sumOfAll += Math.round(Math.pow(n + 1, i) * Math.pow(n, level - i));
      }

      return new Outputer(numOfNonWorkingCats, sumOfAll);
    }

  }

  private static class Outputer {

    private long numOfNonWorkingCats;
    private long sumOfAll;

    public Outputer(long numOfNonWorkingCats2, long sumOfAll2) {
      this.numOfNonWorkingCats = numOfNonWorkingCats2;
      this.sumOfAll = sumOfAll2;
    }

    public void output() {
      System.out.println(numOfNonWorkingCats + " " + sumOfAll);
    }

  }

  public static void main(String[] args) {
    try (Inputs in = new Inputs(System.in)) {
      for (Input input : in) {
        input.getComputer().compute().output();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}

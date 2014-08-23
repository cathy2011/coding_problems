import java.io.InputStream;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Scanner;

public class Fermat {

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

        @Override
        public boolean hasNext() {
          return in.hasNext();
        }

        @Override
        public Input next() {
          return new Input(in.nextInt());
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }

  }

  private static class Input {

    private Integer n;

    public Input(Integer n) {
      this.n = n;
    }

    public Integer getN() {
      return n;
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
      int numOfPrimePairs = 0;
      BitSet bs = new BitSet(input.getN() + 1);
      bs.clear();
      for (int r = 1; r <= Math.sqrt(input.getN()) + 1; ++r) {
        for (int s = 1; s < r; ++s) {
          int x = r * r - s * s;
          int y = 2 * r * s;
          int z = r * r + s * s;
          if (x <= input.getN()
              && y <= input.getN()
              && z <= input.getN()
              && gcd(x, y) == 1 && gcd(y, z) == 1
              && x * x + y * y == z * z) {
            ++numOfPrimePairs;
            for (int k = 1; k * z <= input.getN(); ++k) {
              bs.set(x * k);
              bs.set(y * k);
              bs.set(z * k);
            }
          }
        }
      }

      return new Outputer(numOfPrimePairs, input.getN() - bs.cardinality());
    }
    
    private int gcd(int a, int b) {
      if (b == 0) {
        return a;
      }
      return gcd(b, a % b);
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

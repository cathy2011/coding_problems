// Simple math.
// 14100540 121 Pipe Fitters Accepted JAVA 0.215  2014-08-27 01:37:24 
import java.util.Scanner;

public class PipeFitters {
  
  private static long getNumForGrid(double a, double b) {
    return Math.round(Math.floor(a) * Math.floor(b));
  }
  
  private static long getNumForSkew(double a, double b) {
    long numOfLevels = Math.round(Math.floor(1.0 + 2.0 * (b - 1.0) / Math.sqrt(3.0)));
    long even = Math.round(a - Math.floor(a) >= 0.5 ? Math.floor(a) : Math.floor(a) - 1);
    long res = Math.round((numOfLevels / 2) * (Math.floor(a) + even) + (numOfLevels % 2 == 0 ? 0 : Math.floor(a)));
    return res;
  }

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in);) {
      double a;
      double b;
      while (in.hasNextDouble()) {
        a = in.nextDouble();
        b = in.nextDouble();
        long aa = getNumForGrid(a, b);
        long bb = Math.max(getNumForSkew(a, b), getNumForSkew(b, a));
        if (aa >= bb) {
          System.out.println(aa + " grid");
        } else {
          System.out.println(bb + " skew");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}

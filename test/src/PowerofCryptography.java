import java.util.Scanner;

public class PowerofCryptography {

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in);) {
      while (in.hasNextDouble()) {
        double n = in.nextDouble();
        double p = in.nextDouble();
        System.out.println(Math.round(Math.exp(Math.log(p) / n)));
      }
    }
  }

}

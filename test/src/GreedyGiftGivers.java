import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// 119 Greedy Gift Givers
public class GreedyGiftGivers {

  private static void process(Scanner in) {
    final List<String> names = new ArrayList<String>();
    // Save each person's net gain/loss.
    final Map<String, Integer> worth = new HashMap<String, Integer>();

    // Read all persons and initialize each's net worth to 0.
    final int numOfPersons = in.nextInt();
    for (int i = 0; i < numOfPersons; ++i) {
      final String name = in.next();
      names.add(name);
      worth.put(name, Integer.valueOf(0));
    }

    // Process giving events, one for each person.
    for (int i = 0; i < numOfPersons; ++i) {
      final String giver = in.next();
      final int gives = in.nextInt();
      int effective = 0;
      final int numOfReceivers = in.nextInt();
      for (int j = 0; j < numOfReceivers; ++j) {
        final String receiver = in.next();
        worth.put(receiver, worth.get(receiver) + gives / numOfReceivers);
        effective += gives / numOfReceivers;
      }
      worth.put(giver, Integer.valueOf(worth.get(giver) - effective));
    }

    // Output the result.
    for (final String name : names) {
      System.out.println(name + " " + worth.get(name));
    }
  }

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in);) {
      boolean firstCase = true;
      while (in.hasNextInt()) {
        if (firstCase) {
          firstCase = false;
        } else {
          System.out.println();
        }
        process(in);
      }
    }
  }

}

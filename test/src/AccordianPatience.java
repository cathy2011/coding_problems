import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

// UVA 127 - "Accordian" Patience
// This java solution failed to pass in uva but passed pku.
// 13397764 xin009  1214  Accepted  2016K 110MS Java  2362B 2014-08-31 08:59:49

public class AccordianPatience {

  static boolean equal(String c1, String c2) {
    return c1.charAt(0) == c2.charAt(0) || c1.charAt(1) == c2.charAt(1);
  }

  public static void main(String[] args) {

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        System.in));) {
      String line = null;
      while ((line = reader.readLine()) != null && !line.startsWith("#")) {
        final String secondLine = reader.readLine();
        final List<List<String>> piles = new ArrayList<List<String>>();
        final StringTokenizer st = new StringTokenizer(line + " " + secondLine);
        while (st.hasMoreTokens()) {
          final List<String> pile = new LinkedList<String>();
          pile.add(st.nextToken());
          piles.add(pile);
        }

        int pos = 0;
        while (pos < piles.size()) {
          while (true) { // Loop until position 'pos' is a dead position.
            int posToCheck = -1;
            if (pos >= 3
                && equal(piles.get(pos - 3).get(0), piles.get(pos).get(0))) {
              piles.get(pos - 3).add(0, piles.get(pos).get(0));
              piles.get(pos).remove(0);
              posToCheck = pos;
              pos = pos - 3;
            } else if (pos >= 1
                && equal(piles.get(pos - 1).get(0), piles.get(pos).get(0))) {
              piles.get(pos - 1).add(0, piles.get(pos).get(0));
              piles.get(pos).remove(0);
              posToCheck = pos;
              pos = pos - 1;
            }
            if (posToCheck >= 0) {
              if (piles.get(posToCheck).isEmpty()) {
                piles.remove(posToCheck);
              }
            } else {
              break;
            }
          }
          ++pos;
        }

        System.out.print(piles.size() + " "
            + (piles.size() == 1 ? "pile" : "piles") + " remaining: ");
        for (pos = 0; pos < piles.size(); ++pos) {
          System.out.print((pos > 0 ? " " : "") + piles.get(pos).size());
        }
        System.out.println();
      }
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}

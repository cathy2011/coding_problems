import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

// 14115756 124 Following Orders  Accepted  JAVA  0.205 2014-08-29 23:28:02

public class FollowingOrders {

  // One single data set.
  private static class DataSet {

    public static DataSet NewDataSet(BufferedReader reader) {
      final DataSet ds = new DataSet();
      String line = null;
      try {
        // Read characters.
        if ((line = reader.readLine()) == null) {
          return null;
        }
        for (int i = 0; i < line.length(); ++i) {
          if (line.charAt(i) >= 'a' && line.charAt(i) <= 'z') {
            ds.add(line.charAt(i));
          }
        }
        // Read constrains.
        line = reader.readLine();
        for (int i = 0, lastPos = -1; i < line.length(); ++i) {
          final char ch = line.charAt(i);
          if (ch >= 'a' && ch <= 'z') {
            if (lastPos < 0) {
              lastPos = i;
            } else { // We have found a pair.
              ds.mustPrecedeConstraint(line.charAt(lastPos), ch);
              lastPos = -1;
            }
          }
        }

        ds.init();
        return ds;
      } catch (final IOException e) {
        return null;
      }
    }

    // Characters in this data set, in ascending order.
    private final SortedSet<Character> chars    = new TreeSet<Character>();
    // precedes[i][j]: whether char i must precedes char j.
    private boolean[][]                precedes = new boolean[26][26];

    private DataSet() {
      for (int i = 0; i < 26; ++i) {
        Arrays.fill(precedes[i], false);
      }
    }

    private void add(Character ch) {
      chars.add(ch);
    }

    public List<Character> getSortedChars() {
      final List<Character> list = new LinkedList<Character>();
      for (final Character ch : chars) {
        list.add(ch);
      }
      return list;
    }

    // a must precede b.
    private void mustPrecedeConstraint(Character a, Character b) {
      precedes[(int) a - (int) 'a'][(int) b - (int) 'a'] = true;
    }

    public boolean precedes(Character a, Character b) {
      return !precedes[(int) b - (int) 'a'][(int) a - (int) 'a'];
    }

    private void init() {
      final boolean[][] result = precedes.clone();
      for (int x = 0; x < chars.size(); ++x) {
        for (int i = 0; i < 26; ++i) {
          for (int j = 0; j < 26; ++j) {
            if (!result[i][j]) {
              for (int k = 0; k < 26; ++k) {
                if (result[i][k] && result[k][j]) {
                  result[i][j] = true;
                  break;
                }
              }
            }
          }
        }
      }
      precedes = result;
    }

  }

  private static class ResultCallback {

    private final TreeSet<String> results = new TreeSet<String>();

    public void processResult(List<Character> result) {
      final StringBuilder builder = new StringBuilder();
      for (final Character ch : result) {
        builder.append(ch);
      }
      results.add(builder.toString());
    }

    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder();
      for (final String result : results) {
        builder.append(result).append("\n");
      }
      return builder.toString();
    }

  }

  private static void search(List<Character> satisfied,
      List<Character> remaining, DataSet ds, ResultCallback cb) {
    if (remaining.isEmpty()) {
      cb.processResult(satisfied);
      return;
    }
    final Character ch = remaining.remove(0);
    for (int pos = satisfied.size(); pos >= 0; --pos) {
      final int prev = pos > 0 ? pos - 1 : -1;
      final int next = pos < satisfied.size() ? pos : -1;
      if ((prev < 0 || ds.precedes(satisfied.get(prev), ch))
          && (next < 0 || ds.precedes(ch, satisfied.get(next)))) {
        final List<Character> backup = new LinkedList<Character>(satisfied);
        satisfied.add(pos, ch);
        search(satisfied, remaining, ds, cb);
        satisfied = backup;
      }
    }
    remaining.add(0, ch);
  }

  public static void main(String[] args) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        System.in));) {
      boolean isFirst = true;
      DataSet ds = null;
      while ((ds = DataSet.NewDataSet(reader)) != null) {
        if (isFirst) {
          isFirst = false;
        } else {
          System.out.println();
        }

        final ResultCallback cb = new ResultCallback();
        search(new LinkedList<Character>(), ds.getSortedChars(), ds, cb);
        System.out.print(cb);
      }
    }
  }

}

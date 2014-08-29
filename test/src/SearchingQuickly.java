import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

// 14110467  123 Searching Quickly Accepted  JAVA  0.169 2014-08-29 00:21:37
public class SearchingQuickly {

  // One single index entry.
  public static class Entry implements Comparable<Entry> {

    // All should be in lower case.
    private final String  keyword;
    private final String  title;
    private final Integer titleIndex;
    private final Integer position;

    public Entry(String keyword, String title, Integer titleIndex,
        Integer position) {
      this.keyword = keyword.toUpperCase();
      this.title = title;
      this.titleIndex = titleIndex;
      this.position = position;
    }

    @Override
    public String toString() {
      return title.substring(0, position) + keyword
          + title.substring(position + keyword.length());
    }

    @Override
    public int compareTo(Entry o) {
      int result = this.keyword.compareTo(o.keyword);
      if (result == 0) {
        result = this.titleIndex - o.titleIndex;
        if (result == 0) {
          result = this.position - o.position;
        }
      }
      return result;
    }

  }

  public static void main(String[] args) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));) {
      String line = null;
      Set<String> ignored = new HashSet<String>();
      while ((line = reader.readLine()) != null && !line.equals("::")) {
        ignored.add(line.toLowerCase());
      }
      Set<Entry> entries = new TreeSet<Entry>();
      int count = 0;
      while ((line = reader.readLine()) != null) {
        line = line.toLowerCase();
        // Process this line.
        int currentWordStart = -1;
        int currentPosition = 0;
        while (currentPosition <= line.length()) {
          if (currentPosition < line.length()
              && line.charAt(currentPosition) != ' '
              && currentWordStart < 0) {
            // Start of a word
            currentWordStart = currentPosition;
          } else if ((currentPosition == line.length() || line.charAt(currentPosition) == ' ') 
                     && currentWordStart >= 0) {
            // End of a word
            String word = line.substring(currentWordStart, currentPosition);
            if (!ignored.contains(word)) {
              entries.add(new Entry(word, line, count, currentWordStart));
            }
            currentWordStart = -1;  // This is very important!!!
          }
          ++currentPosition;
        }
        ++count;
      }
      for (Entry e : entries) {
        System.out.println(e);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}

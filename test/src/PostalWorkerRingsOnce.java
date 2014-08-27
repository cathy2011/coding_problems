// Shortest path.
// 14100473 117 The Postal Worker Rings Once Accepted JAVA 0.178  2014-08-27 01:14:29 

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

public class PostalWorkerRingsOnce {

  // One single input data set.
  private static class Input {

    static public int charToInt(Character c) {
      int ret = (int) c - (int) 'a';
      assert ret >= 0 && ret < 26 : "Invalid char: " + c;
      return ret;
    }

    static public Character intToChar(int n) {
      assert n >= 0 && n < 26 : "Invalid int index: " + n;
      return Character.valueOf((char) ('a' + n));
    }

    // Distance matrix as well as source and dest nodes.
    private int[][] distance;
    private Character source;
    private Character dest;

    public Input() {
      distance = new int[26][26];
      for (int i = 0; i < 26; ++i) {
        for (int j = 0; j < 26; ++j) {
          distance[i][j] = 0; // unreachable
        }
      }
      source = null;
      dest = null;
    }

    public Character getSource() {
      return source;
    }

    public Character getDest() {
      return dest;
    }

    public void setDistanceBeween(Character from, Character to, int distance) {
      this.distance[charToInt(from)][charToInt(to)] = distance;
      this.distance[charToInt(to)][charToInt(from)] = distance;
    }

    public int getDistanceBeween(Character from, Character to) {
      return distance[charToInt(from)][charToInt(to)];
    }

    public int getSumOfAllPaths() {
      int sum = 0;
      for (int i = 0; i < distance.length; ++i) {
        for (int j = 0; j < i; ++j) {
          sum += distance[i][j];
        }
      }
      return sum;
    }

    public void init() {
      for (int node = 0; node < distance.length; ++node) {
        int degree = 0;
        for (int to = 0; to < distance.length; ++to) {
          if (distance[node][to] > 0) {
            ++degree;
          }
        }
        if (degree % 2 == 1) { // odd degree
          assert source == null || dest == null : "More than 2 nodes have odd degree";
          if (source == null) {
            source = intToChar(node);
          } else {
            dest = intToChar(node);
          }
        }
      }
    }

  }

  private static class InputIterator implements Iterator<Input>, AutoCloseable {

    private static final String CASE_END = "deadend";

    private Scanner in = new Scanner(System.in);

    @Override
    public boolean hasNext() {
      return in.hasNext();
    }

    @Override
    public Input next() {
      Input dataset = new Input();

      String path = null;
      while (in.hasNext()) {
        path = in.next();
        if (path.equals(CASE_END)) {
          break;
        }
        dataset.setDistanceBeween(path.charAt(0), path.charAt(path.length() - 1), path.length());
      }

      dataset.init();
      return dataset;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws Exception {
      in.close();
    }

  }

  private static class Computer {

    private Input dataset;

    public Computer(Input dataset) {
      this.dataset = dataset;
    }

    public int compute() {
      int sum = dataset.getSumOfAllPaths();
      Character source = dataset.getSource();
      Character dest = dataset.getDest();
      if (source == null && dest == null) { // We are in a perfect state.
        return sum;
      }
      assert source != null && dest != null && !Objects.equals(source, dest);

      // Now we run shortest-path finding algorithm.
      int[] dist = new int[26];
      Arrays.fill(dist, Integer.MAX_VALUE);
      boolean[] processed = new boolean[26];
      Arrays.fill(processed, false);
      // Initial state
      processed[Input.charToInt(source)] = true;
      for (int to = 0; to < 26; ++to) {
        if (!processed[to] && dataset.getDistanceBeween(source, Input.intToChar(to)) > 0) {
          dist[to] = dataset.getDistanceBeween(source, Input.intToChar(to));
        }
      }
      // Done with one unprocessed within each iteration.
      Character doneChar = source;
      while (!Objects.equals(doneChar, dest)) {
        int tmpMin = Integer.MAX_VALUE;
        for (int i = 0; i < 26; ++i) {
          if (!processed[i] && dist[i] < tmpMin) {
            tmpMin = dist[i];
            doneChar = Input.intToChar(i);
          }
        }
        if (Objects.equals(doneChar, dest)) {
          break;
        }
        // Move doneChar to the set of processed.
        processed[Input.charToInt(doneChar)] = true;
        // And update dist[] accordingly.
        for (int i = 0; i < 26; ++i) {
          if (!processed[i]
              && dataset.getDistanceBeween(doneChar, Input.intToChar(i)) > 0
              && dist[i] > dist[Input.charToInt(doneChar)]
                  + dataset.getDistanceBeween(doneChar, Input.intToChar(i))) {
            dist[i] = dist[Input.charToInt(doneChar)]
                + dataset.getDistanceBeween(doneChar, Input.intToChar(i));
          }
        }
      }

      return sum + dist[Input.charToInt(doneChar)];
    }

  }

  public static void main(String[] args) throws Exception {
    try (InputIterator in = new InputIterator();) {
      while (in.hasNext()) {
        System.out.println(new Computer(in.next()).compute());
      }
    }
  }

}

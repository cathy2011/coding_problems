import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Skyline {

  private static final int DIM = 3;

  private static class Input implements AutoCloseable {

    private Scanner in = null;

    public Input(InputStream in) {
      this.in = new Scanner(in);
    }

    @Override
    public void close() throws Exception {
      if (this.in != null) {
        this.in.close();
      }
    }

    public Iterator<int[]> getBuildingIterator() {
      return new Iterator<int[]>() {

        @Override
        public boolean hasNext() {
          return in.hasNext();
        }

        @Override
        public int[] next() {
          int[] building = new int[DIM];
          for (int i = 0; i < DIM; ++i) {
            building[i] = in.nextInt();
          }
          return building;
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }

      };
    }

    public Computer getComputer() {
      return new Computer(this);
    }

  }

  private static class Computer {

    private static class Event implements Comparable<Event> {

      public enum EventType {
        IN, OUT
      }

      private int x;
      private int h;
      private EventType type;

      public Event(int x, int h, EventType type) {
        this.x = x;
        this.h = h;
        this.type = type;
      }

      public int getX() {
        return x;
      }

      public int getH() {
        return h;
      }

      public EventType getType() {
        return type;
      }

      @Override
      public int compareTo(Event o) {
        return Integer.compare(this.x, o.x);
      }

    }

    private Input input;

    public Computer(Input input) {
      this.input = input;
    }

    public Output compute() {
      List<Event> events = getSortedEvents();

      // State variables
      PriorityQueue<Integer> sortedHeights = new PriorityQueue<Integer>(1,
          new Comparator<Integer>() {

            @Override
            public int compare(Integer arg0, Integer arg1) {
              return Integer.compare(arg1, arg0);
            }

          });
      Integer currentHighest = sortedHeights.peek();
      List<Integer> outputs = new LinkedList<Integer>();
      int curPosition = Integer.MIN_VALUE;
      for (Event event : events) {
        Integer prevHighest = currentHighest;
        if (event.getType() == Event.EventType.IN) {
          sortedHeights.add(event.getH());
        } else if (event.getType() == Event.EventType.OUT) {
          sortedHeights.remove(event.getH());
        }
        currentHighest = sortedHeights.peek();
        if (event.getX() > curPosition && !Objects.equals(prevHighest, currentHighest)) { // Moved
                                                                                          // and
                                                                                          // new
                                                                                          // high
                                                                                          // changed.
          if (curPosition > Integer.MIN_VALUE) {
            outputs.add(curPosition);
            outputs.add(prevHighest == null ? 0 : prevHighest);
          }
          curPosition = event.getX();
        }
      }

      return new Output(outputs);
    }

    private List<Event> getSortedEvents() {
      List<Event> events = new LinkedList<Event>();
      Iterator<int[]> buildingIterator = input.getBuildingIterator();
      while (buildingIterator.hasNext()) {
        int[] building = buildingIterator.next();
        events.add(new Event(building[0], building[1], Event.EventType.IN));
        events.add(new Event(building[2], building[1], Event.EventType.OUT));
      }
      // The special end event
      events.add(new Event(Integer.MAX_VALUE, Integer.MAX_VALUE, Event.EventType.IN));
      Collections.sort(events);
      return events;
    }

  }

  private static class Output {

    private List<Integer> output;

    public Output(List<Integer> output) {
      this.output = output;
    }

    public void output() {
      StringBuilder builder = new StringBuilder();
      Iterator<Integer> it = output.iterator();
      if (it.hasNext()) {
        builder.append(it.next());
      }
      while (it.hasNext()) {
        builder.append(' ').append(it.next());
      }
      System.out.println(builder);
    }

  }

  public static void main(String[] args) {
    try (Input in = new Input(System.in);) {
      // try (Input in = new Input(new FileInputStream("c://Users//QQ//Desktop//test.txt"));) {
      in.getComputer().compute().output();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}

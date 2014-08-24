// Got WA for several times because double precision problems.
// Use integers whenever you can, in order to not lose any
// precision.

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SCUDBusters {

  private static class Point {

    // p0_p1 X p0_p2
    public static int cross(Point p0, Point p1, Point p2) {
      int p01X = p1.getX() - p0.getX();
      int p01Y = p1.getY() - p0.getY();
      int p02X = p2.getX() - p0.getX();
      int p02Y = p2.getY() - p0.getY();
      return p01X * p02Y - p01Y * p02X;
    }

    private int x;
    private int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

  }

  private static class Kingdom {

    // All homes existing in this kingdom.
    List<Point> homes;
    // Convex Hull
    List<Point> convex;
    // Attacked already?
    private boolean attacked = false;

    public Kingdom(Scanner in) throws Exception {
      int numOfHomes = in.nextInt();
      if (numOfHomes == -1) {
        throw new Exception("no more kingdoms");
      }
      homes = new ArrayList<Point>(numOfHomes);
      while (numOfHomes-- > 0) {
        homes.add(new Point(in.nextInt(), in.nextInt()));
      }
      computeConvex();
    }

    public int getArea() {
      int area = 0;
      for (int i = 2; i < convex.size(); ++i) {
        area += Point.cross(convex.get(0), convex.get(i), convex.get(i - 1));
      }
      return area;
    }

    public boolean contains(Point p) {
      for (int i = 0; i < convex.size(); ++i) {
        if (Point.cross(convex.get(i), convex.get((i + 1) % convex.size()), p) >= 0) {
          return false;
        }
      }
      return true;
    }

    public boolean isAttacked() {
      return attacked;
    }

    public void setAttacked(boolean attacked) {
      this.attacked = attacked;
    }

    private void computeConvex() {
      // Find the left-most home.
      Point leftMostHome = null;
      for (Point h : homes) {
        if (leftMostHome == null || h.getX() < leftMostHome.getX()
            || h.getX() == leftMostHome.getX() && h.getY() > leftMostHome.getY()) {
          leftMostHome = h;
        }
      }
      // Compute convex of this kingdom.
      convex = new ArrayList<Point>();
      Point pos = leftMostHome;
      do {
        convex.add(pos);

        // Compute the next home.
        Point next = null;
        for (Point h : homes) {
          if (h == pos) {
            continue;
          }
          if (next == null || Point.cross(pos, h, next) < 0) {
            next = h;
          }
        }
        pos = next;
      } while (pos != leftMostHome);
    }
  }

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in);) {
      List<Kingdom> kingdoms = new ArrayList<Kingdom>();
      while (true) {
        try {
          kingdoms.add(new Kingdom(in));
        } catch (Exception e) {
          break;
        }
      }

      int affectedArea = 0;
      while (in.hasNext()) {
        Point attackPoint = new Point(in.nextInt(), in.nextInt());
        for (Kingdom kingdom : kingdoms) {
          if (!kingdom.isAttacked() && kingdom.contains(attackPoint)) {
            affectedArea += kingdom.getArea();
            kingdom.setAttacked(true);
          }
        }
      }
      System.out.println(affectedArea / 2 + "." + (affectedArea % 2 == 0 ? "00" : "50"));
    }
  }

}

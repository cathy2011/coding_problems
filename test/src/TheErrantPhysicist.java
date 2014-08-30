import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// 14120487  126 The Errant Physicist  Accepted  JAVA  0.162 2014-08-30 20:39:14
public class TheErrantPhysicist {

  // A single term.
  private static class Term implements Comparable<Term> {

    // Construct a term from a given string, like "-2x45y2".
    public static Term fromString(String s) {
      final int[] c = { 0, 0, 0 };
      for (int begin = 0, pos = 0, i = 0; i < s.length() + 1; ++i) {
        if (i == s.length() || s.charAt(i) == 'x' || s.charAt(i) == 'y') {
          String tmp = s.substring(begin, i);
          if (tmp.isEmpty() || tmp.equals("-") || tmp.equals("+")) {
            tmp = tmp + "1";
          }
          c[pos] = Integer.valueOf(tmp);
          begin = i + 1;
          if (i < s.length()) {
            if (s.charAt(i) == 'x') {
              pos = 1;
            } else { // y
              pos = 2;
            }
          }
        }
      }
      return new Term(c[0], c[1], c[2]);
    }

    private int       coefficient;
    private final int expX;
    private final int expY;

    private Term(int coefficient, int expX, int expY) {
      this.coefficient = coefficient;
      this.expX = expX;
      this.expY = expY;
    }

    // Merge another term into this. Return reference to this term.
    public Term merge(Term another) {
      if (compareTo(another) == 0) {
        coefficient += another.coefficient;
      }
      return this;
    }

    public Term multiply(Term another) {
      return new Term(coefficient * another.coefficient, expX + another.expX,
          expY + another.expY);
    }

    @Override
    public int compareTo(Term another) {
      int result = another.expX - expX;
      if (result == 0) {
        result = expY - another.expY;
      }
      return result;
    }

    private String padding(int len) {
      final StringBuilder builder = new StringBuilder();
      for (int i = 0; i < len; ++i) {
        builder.append(' ');
      }
      return builder.toString();
    }

    public String[] rep(boolean isFirst) {
      final String[] results = { "", "" };
      if (coefficient != 0) {
        String sign = "";
        if (isFirst) {
          sign = coefficient > 0 ? "" : "-";
        } else {
          sign = coefficient > 0 ? " + " : " - ";
        }
        final String c = (expX != 0 || expY != 0) && Math.abs(coefficient) == 1 ? ""
            : String.format("%d", Math.abs(coefficient));
        final String x = expX == 0 ? "" : "x";
        final String ex = expX == 0 || expX == 1 ? "" : String.format("%d",
            expX);
        final String y = expY == 0 ? "" : "y";
        final String ey = expY == 0 || expY == 1 ? "" : String.format("%d",
            expY);

        results[0] = padding(sign.length() + c.length() + x.length()) + ex
            + padding(y.length()) + ey;
        results[1] = sign + c + x + padding(ex.length()) + y
            + padding(ey.length());
      }
      return results;
    }

  }

  private static void print(Collection<Term> terms) {
    final List<String[]> results = new LinkedList<String[]>();
    boolean isFirst = true;
    for (final Term t : terms) {
      results.add(t.rep(isFirst));
      isFirst = false;
    }
    for (int i = 0; i < 2; ++i) {
      for (final String[] s : results) {
        System.out.print(s[i]);
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));) {
      // try (BufferedReader r = new BufferedReader(new
      // FileReader("C:\\Users\\QQ\\Desktop\\test.txt"));) {
      String line = null;
      while ((line = r.readLine()) != null && !line.startsWith("#")) {
        final List<Term> p1 = new LinkedList<Term>();
        for (int begin = 0, pos = 1; pos < line.length() + 1; ++pos) {
          if (pos == line.length() || line.charAt(pos) == '+'
              || line.charAt(pos) == '-') {
            p1.add(Term.fromString(line.substring(begin, pos)));
            begin = pos;
          }
        }
        line = r.readLine();
        final List<Term> p2 = new LinkedList<Term>();
        for (int begin = 0, pos = 1; pos < line.length() + 1; ++pos) {
          if (pos == line.length() || line.charAt(pos) == '+'
              || line.charAt(pos) == '-') {
            p2.add(Term.fromString(line.substring(begin, pos)));
            begin = pos;
          }
        }
        // p1 * p2
        final List<Term> p = new LinkedList<Term>();
        for (final Term t1 : p1) {
          for (final Term t2 : p2) {
            p.add(t1.multiply(t2));
          }
        }
        Collections.sort(p);
        // merge
        final List<Term> res = new LinkedList<Term>();
        Term last = null;
        for (final Term t : p) {
          if (last == null) {
            last = t;
          } else if (last.compareTo(t) == 0) {
            last.merge(t);
          } else {
            res.add(last);
            last = t;
          }
        }
        if (last != null) {
          res.add(last);
        }
        // output
        print(res);
      }
    } catch (final IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}

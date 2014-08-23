import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MetaLooplessSort {
  
  private static class Computer {
 
    private int n;
    private StringBuilder result = new StringBuilder();
    
    private StringBuilder indentAtLevel(int level) {
      for (int i = 0; i <= level; ++i) {
        result.append(' ');
      }
      return result;
    }
    
    private char toChar(int value) {
      return (char) ('a' + value);
    }
    
    private String getVarList() {
      StringBuilder r = new StringBuilder();
      for (int i = 0; i < n; ++i) {
        r.append(toChar(i)).append(i == n - 1 ? "" : ",");
      }
      return r.toString();
    }
    
    // Push the next character into the sorted list.
    private void code(List<Integer> sorted, int nextChar) {
      if (nextChar >= n) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < sorted.size(); ++i) {
          r.append(toChar(sorted.get(i)))
           .append(i == sorted.size() - 1 ? "" : ",");
        }
        indentAtLevel(nextChar).append("writeln(" + r + ")\n");
        return;
      }
      for (int i = sorted.size(); i >= 0; --i) {
        List<Integer> backup = new LinkedList<Integer>(sorted);
        
        // insert nextChar at position i.
        if (i == sorted.size()) {
          indentAtLevel(nextChar).append("if " + toChar(sorted.get(i - 1)) + " < " + toChar(nextChar) + " then\n");
        } else if (i > 0) {
          indentAtLevel(nextChar).append("else if " + toChar(sorted.get(i - 1)) + " < " + toChar(nextChar) + " then\n");
        } else {
          indentAtLevel(nextChar).append("else\n");
        }
        sorted.add(i, nextChar);
        code(sorted, nextChar + 1);
        
        sorted = backup;
      }
    }
    
    public Computer readInput(Scanner in) {
      n = in.nextInt();
      return this;
    }
    
    public Computer compute() {
      List<Integer> list = new LinkedList<Integer>();
      list.add(0);
      code(list, 1);
      return this;
    }
    
    public void outputResult() {
      StringBuilder r = new StringBuilder();
      r.append("program sort(input,output);\n")
       .append("var\n")
       .append(getVarList()).append(" : integer;\n")
       .append("begin\n")
       .append("  readln(" + getVarList() + ");\n")
       .append(result)
       .append("end.");
      System.out.println(r);
    }
  }

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in);) {
      int numOfCases = in.nextInt();
      for (; numOfCases > 0; --numOfCases) {
        new Computer().readInput(in).compute().outputResult();
        if (numOfCases > 1) {
          System.out.println();
        }
      }
    }
  }

}

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

// 14109523 122 Trees on the level  Accepted  JAVA  0.215 2014-08-28 19:02:42
public class TreesOnTheLevel {
  
  // Abstraction for one single input dataset.
  private static class InputTree {
    
    private static final String FIRST_NODE = "";
    
    // Each tree node is represented by a <key, value> pair, where the key is the path from
    // root node to the node. For example, <"LLR", 5>.
    private final Map<String, Integer> nodes = new HashMap<String, Integer>();
    
    // Whether this is a valid tree.
    private boolean valid = true;
    
    // Add a new node into the tree. Integers of the same value should use the same Integer instance.
    public void add(String path, Integer value) {
      Integer oldValue = nodes.put(path, value);
      if (oldValue != null && !oldValue.equals(value)) { valid = false; }
    }
 
    // Output this tree in level order and return whether there are no remaining nodes.
    public boolean toList(List<Integer> list) {
      if (!valid) { return false; }
      Queue<String> queue = new LinkedList<String>();
      if (nodes.containsKey(FIRST_NODE)) { queue.add(FIRST_NODE); }
      while (!queue.isEmpty()) {
        String head = queue.poll();  // Path to the head node.
        list.add(nodes.remove(head));
        if (nodes.containsKey(head + "L")) { queue.add(head + "L"); }
        if (nodes.containsKey(head + "R")) { queue.add(head + "R"); }
      }
      return nodes.isEmpty();
    }
  }
  
  private static class InputTreeIterator implements Iterator<InputTree>, AutoCloseable {
    
    private Scanner in = new Scanner(System.in);

    @Override
    public boolean hasNext() {
      return in.hasNext();
    }

    @Override
    public InputTree next() {
      InputTree tree = new InputTree();
      String node = null;
      while (!(node = in.next()).equals("()")) {
        String[] tokens = node.substring(1, node.length() - 1).split(",");
        assert tokens.length == 1 && tokens.length ==2;
        tree.add(tokens.length < 2 ? "" : tokens[1], Integer.valueOf(tokens[0]));
      }
      return tree;
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
  
  private static void output(List<Integer> list) {
    StringBuilder builder = null;
    for (Integer n : list) {
      if (builder == null) {
        builder = new StringBuilder();
      } else {
        builder.append(" ");
      }
      builder.append(n);
    }
    System.out.println(builder);
  }

  public static void main(String[] args) throws Exception {
    try (InputTreeIterator it = new InputTreeIterator();) {
      while (it.hasNext()) {
        List<Integer> list = new LinkedList<Integer>();
        if (it.next().toList(list)) {
          output(list);
        } else {
          System.out.println("not complete");
        }
      }
    }
  }

}

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * 115 - Climbing Trees
 * 
 * @author QQ
 *
 */
public class ClimbingTrees {

  // String constants.
  public static final String NO_RELATION = "no relation";
  public static final String CHILD = "child";
  public static final String PARENT = "parent";
  public static final String SIBLING = "sibling";
  public static final String COUSIN = "cousin";

  public static final String GREAT = "great";
  public static final String GRAND = "grand";

  /**
   * Abstraction for a single person.
   * 
   * @author QQ
   *
   */
  private static class Person {

    // All Person instances, with one per each unique name string.
    public static HashMap<String, Person> allPersons = new HashMap<String, Person>();

    // Factory method for creating Person instances.
    public static Person create(String name) {
      Person p = allPersons.get(name);
      if (p != null) {
        return p;
      }
      p = new Person(name);
      allPersons.put(name, p);
      return p;
    }

    // Clear all person's visiting related information.
    public static void resetAllPersons() {
      for (Person p : allPersons.values()) {
        p.reset();
      }
    }

    private Person(String name) {
      this.name = name;
      this.parents = new HashSet<Person>();
      this.children = new HashSet<Person>();
      reset();
    }

    // Name, all parents, and children of this person.
    private String name;
    private HashSet<Person> parents;
    private HashSet<Person> children;

    // BFS-visiting related information of this person.
    private boolean visited;
    // How this person has been reached from elsewhere.
    private int ups;
    private int downs;

    public boolean isVisited() {
      return visited;
    }

    public void setVisited() {
      this.visited = true;
    }

    // Reset visiting-related information.
    public void reset() {
      this.visited = false;
      setHow(0, 0);
    }

    public void setHow(int ups, int downs) {
      this.ups = ups;
      this.downs = downs;
    }

    public int getUps() {
      return ups;
    }

    public int getDowns() {
      return downs;
    }

    public String getRelationship() {
      if (!visited) {
        return NO_RELATION;
      }
      if (ups == 0) { // Child case.
        if (downs == 0) {
          return NO_RELATION;
        }
        StringBuilder builder = new StringBuilder();
        while (downs > 2) {
          builder.append(GREAT + " ");
          --downs;
        }
        if (downs > 1) {
          builder.append(GRAND + " ");
          --downs;
        }
        builder.append(CHILD);
        return builder.toString();
      } else if (downs == 0) { // Parent case
        StringBuilder builder = new StringBuilder();
        while (ups > 2) {
          builder.append(GREAT + " ");
          --ups;
        }
        if (ups > 1) {
          builder.append(GRAND + " ");
          --ups;
        }
        builder.append(PARENT);
        return builder.toString();
      } else { // Cousin and sibling cases.
        --ups;
        --downs;
        int k = Math.min(ups, downs);
        int j = Math.abs(ups - downs);
        if (k == 0 && j == 0) {
          return SIBLING;
        }
        if (j == 0) {
          return k + " " + COUSIN;
        }
        return k + " " + COUSIN + " removed " + j;
      }
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
    }

    public HashSet<Person> getParents() {
      return parents;
    }

    public HashSet<Person> getChildren() {
      return children;
    }

  }

  // Use BFS to search for p2 starting from p1.
  public static String figureOutRelationship(Person p1, Person p2) {
    if (p1 == null || p2 == null) {
      return NO_RELATION;
    }

    Person.resetAllPersons();

    Queue<Person> queue = new LinkedList<Person>();
    queue.add(p1);
    while (!queue.isEmpty()) {
      Person head = queue.remove();
      head.setVisited();

      if (head == p2) { // p2 reached/found.
        return head.getRelationship();
      }

      // Add all unvisited parents.
      for (Person parent : head.getParents()) {
        if (parent.isVisited()) {
          continue;
        }
        parent.setHow(head.getUps() + 1, head.getDowns());
        queue.add(parent);
      }
      // Add all unvisited children.
      for (Person child : head.getChildren()) {
        if (child.isVisited()) {
          continue;
        }
        child.setHow(head.getUps(), head.getDowns() + 1);
        queue.add(child);
      }
    }
    return NO_RELATION;
  }

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in);) {
      // read relationship pairs.
      while (true) {
        String parentName = in.next();
        String childName = in.next();
        if (parentName.equals("no.child")) {
          break;
        }
        Person parent = Person.create(parentName);
        Person child = Person.create(childName);
        parent.getChildren().add(child);
        child.getParents().add(parent);
      }
      while (in.hasNext()) {
        System.out.println(figureOutRelationship(Person.allPersons.get(in.next()),
            Person.allPersons.get(in.next())));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

class Box {

  private Box prev;
  private Box next;
  private Box head;
  private Integer id;

  public Box(Integer id) {
    this.id = id;
    prev = null;
    next = null;
    head = null;
  }

  public Box getPrev() {
    return prev;
  }

  public void setPrev(Box prev) {
    this.prev = prev;
  }

  public Box getNext() {
    return next;
  }

  public void setNext(Box next) {
    this.next = next;
  }

  public Box getHead() {
    return head;
  }

  public void setHead(Box head) {
    this.head = head;
  }

  public Integer getId() {
    return id;
  }

}

// Template class for all commands.
abstract class Command {

  protected ArrayList<Box> home;

  public Command(ArrayList<Box> home) {
    this.home = home;
  }

  // Move all boxes on b to their original positions.
  protected void reset(Box b) {
    Box next = b.getNext();
    if (next == null) {
      return;
    }

    reset(next);

    // Time to reset 'next'.
    b.setNext(null);
    next.setPrev(null);
    Box original = home.get(next.getId());
    original.setNext(next);
    next.setPrev(original);
    next.setHead(original);
  }

  protected void pile(Box from, Box to) {
    // Disconnect 'from' with old previous node.
    from.getPrev().setNext(null);
    from.setPrev(null);
    // Find its new previous node.
    while (to.getNext() != null) {
      to = to.getNext();
    }
    // Reconnect 'from' with 'to'.
    from.setPrev(to);
    to.setNext(from);
    // Update nodes in 'from' to point to the right head.
    while (from != null) {
      from.setHead(to.getHead());
      from = from.getNext();
    }
  }

  protected boolean inSamePile(Box from, Box to) {
    return from.getHead() == to.getHead();
  }

  public abstract void execute();

}

class MoveOnto extends Command {

  private Box from;
  private Box to;

  public MoveOnto(ArrayList<Box> home, Box from, Box to) {
    super(home);
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute() {
    if (!inSamePile(from, to)) {
      reset(from);
      reset(to);
      pile(from, to);
    }
  }

}

class MoveOver extends Command {

  private Box from;
  private Box to;

  public MoveOver(ArrayList<Box> home, Box from, Box to) {
    super(home);
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute() {
    if (!inSamePile(from, to)) {
      reset(from);
      pile(from, to);
    }
  }

}

class PileOnto extends Command {

  private Box from;
  private Box to;

  public PileOnto(ArrayList<Box> home, Box from, Box to) {
    super(home);
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute() {
    if (!inSamePile(from, to)) {
      reset(to);
      pile(from, to);
    }
  }

}

class PileOver extends Command {

  private Box from;
  private Box to;

  public PileOver(ArrayList<Box> home, Box from, Box to) {
    super(home);
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute() {
    if (!inSamePile(from, to)) {
      pile(from, to);
    }
  }

}

class Quit extends Command {

  public Quit(ArrayList<Box> home) {
    super(home);
  }

  @Override
  public void execute() {
    StringBuilder builder = new StringBuilder();
    for (Box head : home) {
      builder.append(head.getId() + ":");
      Box next = head.getNext();
      while (next != null) {
        builder.append(" " + next.getId());
        next = next.getNext();
      }
      builder.append('\n');
    }
    System.out.print(builder.toString());
  }

}

class CommandIterator implements Iterator<Command>, Iterable<Command> {

  private Scanner input;
  private ArrayList<Box> boxes;
  private ArrayList<Box> home;

  public CommandIterator(Scanner input, ArrayList<Box> boxes, ArrayList<Box> home) {
    this.input = input;
    this.boxes = boxes;
    this.home = home;
  }

  @Override
  public boolean hasNext() {
    return input.hasNext();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Command next() {
    String cmd, subCmd;
    int from, to;

    cmd = input.next();
    if (cmd.equals("quit")) {
      return new Quit(home);
    } else {
      from = input.nextInt();
      subCmd = input.next();
      to = input.nextInt();
      if (cmd.equals("move")) {
        if (subCmd.equals("onto")) {
          return new MoveOnto(home, boxes.get(from), boxes.get(to));
        } else if (subCmd.equals("over")) {
          return new MoveOver(home, boxes.get(from), boxes.get(to));
        }
      } else { // pile
        if (subCmd.equals("onto")) {
          return new PileOnto(home, boxes.get(from), boxes.get(to));
        } else if (subCmd.equals("over")) {
          return new PileOver(home, boxes.get(from), boxes.get(to));
        }
      }
    }
    return null;
  }

  @Override
  public Iterator<Command> iterator() {
    return this;
  }

}

public class BlocksProblem {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    try {
      final int numBoxes = in.nextInt();
      ArrayList<Box> boxes = new ArrayList<Box>(numBoxes);
      ArrayList<Box> home = new ArrayList<Box>(numBoxes);
      for (int i = 0; i < numBoxes; ++i) {
        Box box = new Box(i);
        boxes.add(box);

        Box fake = new Box(i);
        fake.setNext(box);
        box.setPrev(fake);
        box.setHead(fake);
        home.add(fake);
      }

      for (Command cmd : new CommandIterator(in, boxes, home)) {
        cmd.execute();
      }
    } finally {
      in.close();
    }

  }

}

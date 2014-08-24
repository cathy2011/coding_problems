import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * UVA OJ 112 - Tree Summing
 * 
 * @author QQ
 *
 */
public class TreeSumming {

  private static class TreeNode {

    private int value;
    private TreeNode leftChild;
    private TreeNode rightChild;

    public TreeNode(int value, TreeNode leftChild, TreeNode rightChild) {
      this.value = value;
      this.leftChild = leftChild;
      this.rightChild = rightChild;
    }

    public TreeNode getLeftChild() {
      return leftChild;
    }

    public TreeNode getRightChild() {
      return rightChild;
    }

    public int getValue() {
      return value;
    }

  }

  private static class Input {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("\\(|\\)|(-?\\d+)");

    private int targetSum;
    private TreeNode root;

    public Input(Scanner in) {
      targetSum = in.nextInt();
      root = constructTree(in);
    }

    public int getTargetSum() {
      return targetSum;
    }

    public TreeNode getRoot() {
      return root;
    }

    // Read the next sub-tree from the scanner stream.
    private TreeNode constructTree(Scanner in) {
      TreeNode ret = null;

      String openingParenthesis = in.findWithinHorizon(TOKEN_PATTERN, 0);
      if (Objects.equals(openingParenthesis, "(")) {
        String valueStr = in.findWithinHorizon(TOKEN_PATTERN, 0);
        if (valueStr != null) {
          if (!Objects.equals(valueStr, ")")) {
            TreeNode tmpTree = new TreeNode(Integer.parseInt(valueStr), constructTree(in),
                constructTree(in));
            String closingParenthesis = in.findWithinHorizon(TOKEN_PATTERN, 0);
            if (Objects.equals(closingParenthesis, ")")) {
              ret = tmpTree;
            }
          }
        }
      }

      return ret;
    }
  }

  private static class InputIterator implements Iterator<Input> {

    private Scanner in = null;

    public InputIterator(Scanner in) {
      this.in = in;
    }

    @Override
    public boolean hasNext() {
      return in.hasNextInt();
    }

    @Override
    public Input next() {
      return new Input(in);
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  private static class Computer {

    public Output compute(Input input) {
      return new Output(isAchievable(input.getTargetSum(), input.getRoot()));
    }

    private boolean isAchievable(int sum, TreeNode tree) {
      if (tree == null) {
        return false;
      }
      int remaining = sum - tree.getValue();
      if (remaining == 0 && tree.getLeftChild() == null && tree.getRightChild() == null) {
        return true;
      }
      return isAchievable(remaining, tree.getLeftChild())
          || isAchievable(remaining, tree.getRightChild());
    }

  }

  private static class Output {

    private boolean achievable;

    public Output(boolean achievable) {
      this.achievable = achievable;
    }

    public void output() {
      System.out.println(achievable ? "yes" : "no");
    }

  }

  public static void main(String[] args) {
    try (Scanner in = new Scanner(System.in);) {
      Computer computer = new Computer();
      InputIterator iter = new InputIterator(in);
      while (iter.hasNext()) {
        Input input = iter.next();
        Output output = computer.compute(input);
        output.output();
      }
    }
  }

}

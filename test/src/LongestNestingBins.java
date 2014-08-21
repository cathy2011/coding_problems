import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public final class LongestNestingBins {

  private static class Box implements Comparable<Box> {

    private ArrayList<Integer> mDimentions; // ascending order
    private int mLable;

    public Box(int label, ArrayList<Integer> mDimentions) {
      mLable = label;
      this.mDimentions = mDimentions;
      Collections.sort(this.mDimentions);
    }

    @Override
    public int compareTo(Box o) {
      if (contains(o)) {
        if (o.contains(this)) {
          return 0;
        }
        return 1;
      } else if (o.contains(this)) {
        return -1;
      }
      return 0;
    }

    public boolean contains(Box o) {
      for (int i = 0; i < mDimentions.size(); ++i) {
        if (mDimentions.get(i) <= o.mDimentions.get(i)) {
          return false;
        }
      }
      return true;
    }

    public int getmLable() {
      return mLable;
    }

  }

  private static class Result {

    private int mLongestLen;
    private ArrayList<Integer> mNestingSequence;

    public Result(int mLongestLen, ArrayList<Integer> mNestingSequence) {
      this.mLongestLen = mLongestLen;
      this.mNestingSequence = mNestingSequence;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(mLongestLen);
      builder.append('\n');
      for (int i = 0; i < mNestingSequence.size(); ++i) {
        builder.append(mNestingSequence.get(i));
        if (i < mNestingSequence.size() - 1) {
          builder.append(' ');
        }
      }
      return builder.toString();
    }

  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    try {
      while (sc.hasNext()) {
        int numBoxes = sc.nextInt();
        int dimension = sc.nextInt();
        ArrayList<Box> boxes = new ArrayList<Box>(numBoxes);
        for (int i = 0; i < numBoxes; ++i) {
          ArrayList<Integer> boxDimentions = new ArrayList<Integer>(dimension);
          for (int j = 0; j < dimension; ++j) {
            boxDimentions.add(sc.nextInt());
          }
          boxes.add(new Box(i + 1, boxDimentions));
        }
        new LongestNestingBins(boxes).calculate();
      }
    } finally {
      sc.close();
    }

  }

  private ArrayList<Box> mBoxes;

  private int mNumBoxes;

  public LongestNestingBins(ArrayList<Box> mBoxes) {
    mNumBoxes = mBoxes.size();
    this.mBoxes = mBoxes;
  }

  public void calculate() {
    Collections.sort(mBoxes);

    int[] maxes = new int[mNumBoxes];
    int[] prev = new int[mNumBoxes];
    int max = Integer.MIN_VALUE;
    int max_index = -1;

    // Something to start with.
    maxes[0] = 1;
    prev[0] = -1;

    // Simple DP
    for (int i = 1; i < mBoxes.size(); ++i) {
      maxes[i] = 1;
      prev[i] = -1;
      for (int j = 0; j < i; ++j) {
        if (mBoxes.get(i).contains(mBoxes.get(j)) && maxes[j] + 1 > maxes[i]) {
          maxes[i] = maxes[j] + 1;
          prev[i] = j;
        }
      }
      if (maxes[i] > max) {
        max = maxes[i];
        max_index = i;
      }
    }

    // Compose result
    ArrayList<Integer> maxSequence = new ArrayList<Integer>();
    while (max_index >= 0) {
      maxSequence.add(mBoxes.get(max_index).getmLable());
      max_index = prev[max_index];
    }
    Collections.reverse(maxSequence);

    // Output result.
    System.out.println(new Result(max, maxSequence));

  }

}

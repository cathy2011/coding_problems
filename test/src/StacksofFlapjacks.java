import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// 120 - Stacks of Flapjacks
public class StacksofFlapjacks {

  private static void reverse(int[] arr, int from, int to) {
    for (int i = from, j = to; i < j; ++i, --j) {
      int tmp = arr[i];
      arr[i] = arr[j];
      arr[j] = tmp;
    }
  }

  // Flip a number from its current position to the new one.
  private static void flip(int[] arr, int from, int to, List<Integer> result) {
    assert from <= to;
    final int len = arr.length;
    if (from == to) {
      return;
    }
    // Flip 'from' to the top.
    if (from > 0) {
      reverse(arr, 0, from);
      result.add(len - from);
    }
    // Flip 'from' to 'to'.
    reverse(arr, 0, to);
    result.add(len - to);
  }

  private static void output(List<Integer> result) {
    StringBuilder builder = null;
    for (Integer n : result) {
      if (builder == null) {
        builder = new StringBuilder();
      } else {
        builder.append(" ");
      }
      builder.append(n);
    }
    System.out.println(builder.toString());
  }

  public static void main(String[] args) {
    try (BufferedReader in = new BufferedReader(
        new InputStreamReader(System.in));) {
      String line;
      while ((line = in.readLine()) != null) {
        System.out.println(line); // echo the input.
        String[] numbers = line.split("\\s+");
        int[] arr = new int[numbers.length];
        for (int i = 0; i < numbers.length; ++i) {
          arr[i] = Integer.valueOf(numbers[i]);
        }
        List<Integer> result = new ArrayList<Integer>();
        for (int i = arr.length - 1; i > 0; --i) {
          // Find out which one is the max in [0, i].
          int pos = -1;
          for (int j = 0; j <= i; ++j) {
            if (pos < 0 || arr[j] > arr[pos]) {
              pos = j;
            }
          }
          if (pos < i) {
            flip(arr, pos, i, result);
          }
        }
        result.add(0);
        output(result);
      }
    } catch (IOException e) {

    }
  }

}

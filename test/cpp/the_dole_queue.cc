// 133 - The Dole Queue
// 14131996 133 The Dole Queue  Accepted  C++11 0.009 2014-09-02 01:13:38

#include <algorithm>
#include <array>
#include <functional>
#include <iomanip>
#include <iostream>

namespace {

static const int MAX_NUM_OF_PERSON = 20;

// n: n persons in total.
// k: step for counter-clockwise.
// m: step for clockwise.
void Process(int n, int k, int m) {
  // Doubly-linked list.
  std::array<int, MAX_NUM_OF_PERSON> prev;
  std::array<int, MAX_NUM_OF_PERSON> next;
  for (int i = 0; i < n; ++i) {
    prev[i] = (i + 1) % n;
    next[i] = (i - 1 + n) % n;
  }

  // A function used to move a pointer by a given number of times.
  std::function<void(const std::array<int, MAX_NUM_OF_PERSON>&, int*, int)> advance =
      [](const std::array<int, MAX_NUM_OF_PERSON>& arr, int* p, int times) {
        for (int i = 1; i < times; ++i) {
          *p = arr[*p];
        }
      };

  // Functions for going clockwise and counter-clockwise.
  /*
   std::function<void(int*, int)> clockwise =
   [&next, &advance](int* p, int times) {
   advance(next, p, times);
   };
   std::function<void(int*, int)> counter_clockwise =
   [&prev, &advance](int* p, int times) {
   advance(prev, p, times);
   };
   */
  // ------The following bindings did not work. Not sure why.
  // Now works!!!
  std::function<void(int*, int)> clockwise = std::bind(
      advance, std::cref(next), std::placeholders::_1, std::placeholders::_2);
  std::function<void(int*, int)> counter_clockwise = std::bind(
      advance, std::cref(prev), std::placeholders::_1, std::placeholders::_2);

  int remaining = n;
  int pos_1 = 0;  // goes counter-clockwise.
  int pos_2 = n - 1;  // goes clockwise.
  while (remaining > 0) {
    counter_clockwise(&pos_1, k);
    clockwise(&pos_2, m);
    // Let the two persons leave.
    if (pos_1 == pos_2) {  // They point to the same person.
      std::cout << std::setw(3) << pos_1 + 1;
      if (remaining > 1) {
        next[prev[pos_1]] = next[pos_1];
        prev[next[pos_1]] = prev[pos_1];
        pos_1 = prev[pos_1];
        pos_2 = next[pos_1];
      }
      --remaining;
    } else if (prev[pos_2] == pos_1) {
      // They point to adjacent nodes and pos_1 precedes pos_2.
      std::cout << std::setw(3) << pos_1 + 1 << std::setw(3) << pos_2 + 1;
      if (remaining > 2) {
        next[prev[pos_1]] = next[pos_2];
        prev[next[pos_2]] = prev[pos_1];
        pos_1 = prev[pos_1];
        pos_2 = next[pos_2];
      }
      remaining -= 2;
    } else if (prev[pos_1] == pos_2) {
      // They point to adjacent nodes and pos_2 precedes pos_1.
      std::cout << std::setw(3) << pos_1 + 1 << std::setw(3) << pos_2 + 1;
      if (remaining > 2) {
        next[prev[pos_2]] = next[pos_1];
        prev[next[pos_1]] = prev[pos_2];
        pos_1 = prev[pos_2];
        pos_2 = next[pos_1];
      }
      remaining -= 2;
    } else {
      std::cout << std::setw(3) << pos_1 + 1 << std::setw(3) << pos_2 + 1;
      // Let pos_1 leave.
      next[prev[pos_1]] = next[pos_1];
      prev[next[pos_1]] = prev[pos_1];
      pos_1 = prev[pos_1];
      // Let pos_2 leave.
      next[prev[pos_2]] = next[pos_2];
      prev[next[pos_2]] = prev[pos_2];
      pos_2 = next[pos_2];
      remaining -= 2;
    }
    if (remaining > 0) {
      std::cout << ",";
    }
  }
  std::cout << std::endl;
}

}  // namespace

int main(int argc, char* argv[]) {
  int n, k, m;
  while (std::cin >> n >> k >> m && n && k && m) {
    Process(n, k, m);
  }
  return 0;
}

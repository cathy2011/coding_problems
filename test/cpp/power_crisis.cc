// 151 - Power Crisis

// Simulation

// 14203859 151 Power Crisis  Accepted  C++11 0.013 2014-09-14 21:53:05

#include <iostream>

namespace {

// N in a circle, start from the first and kill every m, return
// the index of the last survivor.
int Simulate(int n, int m) {
  // Who's next after the i'th person.
  int* next = new int[n];
  for (int i = 0; i < n; ++i) {
    next[i] = (i + 1) % n;
  }

  int remaining = n;
  int cur_pos = n - 1;
  while (remaining > 0) {
    // next[cur_pos] is the one to kill.
    next[cur_pos] = next[next[cur_pos]];
    if (--remaining) {
      for (int i = 0; i < m - 1; ++i) {
        cur_pos = next[cur_pos];
      }
    }
  }
  delete[] next;
  return cur_pos;
}

int GetM(int n) {
  for (int m = 1;; ++m) {
    if (Simulate(n, m) == 12) {
      return m;
    }
  }
  return -1;
}

}

int main(int argc, char* argv[]) {

  int n;
  while (std::cin >> n && n) {
    std::cout << GetM(n) << std::endl;
  }

  return 0;
}

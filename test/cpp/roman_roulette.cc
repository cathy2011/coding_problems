#include <iostream>
#include <cstdio>

static const int MAX_NUM_OF_PEOPLE = 100;

static int GetTheSurvivor(int n, int k, int start) {
  // persons[i]: if >=0 denotes this person is still alive AND who's after
  // this person. Otherwise, this person is dead.
  int persons[MAX_NUM_OF_PEOPLE];
  for (int i = 0; i < n; ++i) {  // all alive.
    persons[i] = (i + 1) % n;
  }
  int remaining = n;
  int pos = start;
  int prev = (pos - 1 + remaining) % remaining;
  while (remaining > 1) {
    // Find the person to kill.
    for (int i = 0; i < k - 1; ++i) {
      prev = pos;
      pos = persons[pos];
    }
    persons[prev] = persons[pos];
    persons[pos] = -1;  // The person at position 'pos' gets killed.
    pos = persons[prev];
    if (--remaining == 1) {
      break;
    }

    // Find the person to bury him.
    int tmp_prev = prev;
    int tmp_pos = pos;
    for (int i = 0; i < k - 1; ++i) {
      tmp_prev = tmp_pos;
      tmp_pos = persons[tmp_pos];
    }
    if (tmp_pos != prev && tmp_pos != pos) {
      persons[tmp_prev] = persons[tmp_pos];
      // After burying the victim, go back to the victim's position.
      persons[tmp_pos] = pos;
      persons[prev] = tmp_pos;
    }
  }
  return pos;
}

static int FindStartPosition(int n, int k) {
  for (int i = 0; i < n; ++i) {
    if (GetTheSurvivor(n, k, i) == 0) {
      return i + 1;
    }
  }
  return -1;
}

int main(int argc, char* argv[]) {

  int n, k;
  while (std::cin >> n >> k && n && k) {
    printf("%d\n", FindStartPosition(n, k));
  }

  return 0;
}

// 152 - Tree's a crowd

// 14204220 152 Tree's a Crowd  Accepted  C++11 0.059 2014-09-15 00:19:2

#include <algorithm>
#include <cmath>
#include <iomanip>
#include <iostream>
#include <iterator>
#include <limits>

int main(int argc, char* argv[]) {
  static const int N = 5001;  // WA here with 5000.

  int x[N], y[N], z[N];
  int dist[N];
  int freq[10];

  int trees = 0;
  while (std::cin >> x[trees] >> y[trees] >> z[trees] && (x[trees] || y[trees] || z[trees])) {
    ++trees;
  }

  std::fill(std::begin(dist), std::end(dist), std::numeric_limits<int>::max());
  for (int i = 1; i < trees; ++i) {
    for (int j = 0; j < i; ++j) {
      int d = (x[j] - x[i]) * (x[j] - x[i]) + (y[j] - y[i]) * (y[j] - y[i]) + (z[j] - z[i]) * (z[j] - z[i]);
      if (d < dist[i]) { dist[i] = d; }
      if (d < dist[j]) { dist[j] = d; }
    }
  }

  std::fill(std::begin(freq), std::end(freq), 0);
  for (int i = 0; i < 10; ++i) {
    for (const int& d : dist) {
      if (d >= i * i && d < (i + 1) * (i + 1)) {
        ++freq[i];
      }
    }
  }

  std::for_each(std::begin(freq), std::end(freq), [](const int& f) {
    std::cout << std::setw(4) << f;
  });
  std::cout << std::endl;

  return 0;
}

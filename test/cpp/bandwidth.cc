// 140 Bandwidth

// 14158624 140 Bandwidth Accepted  C++11 0.079 2014-09-06 15:54:31

#include <algorithm>
#include <functional>
#include <iostream>
#include <limits>
#include <string>

int main(int argc, char* argv[]) {

  std::string input;
  while (std::cin >> input && input != "#") {
    std::string nodes;  // All nodes.

    // Read connectivity data.
    std::string conn[26];
    char from = '\0';
    for (const char& c : input) {
      if (c == ';') {  // A new record starts.
        from = '\0';
      } else if (c >= 'A' && c <= 'Z') {
        if (nodes.find(c) == std::string::npos) {
          nodes.append(1, c);
        }

        if (from == '\0') {
          from = c;
        } else {
          conn[from - 'A'].append(1, c);
          conn[c - 'A'].append(1, from);
        }
      }
    }

    // for each permutation, calculate its bandwidth.
    int min_bandwidth = std::numeric_limits<int>::max();
    std::string ordering;
    std::sort(nodes.begin(), nodes.end());
    do {
      int position[26];
      for (unsigned i = 0; i < nodes.length(); ++i) {
        position[nodes[i] - 'A'] = i;
      }
      int bandwidth = -1;  // Bandwidth of this ordering.
      for (unsigned i = 0; i < nodes.length(); ++i) {
        for (unsigned j = 0; j < conn[nodes[i] - 'A'].length(); ++j) {
          int dist = std::abs(
              position[nodes[i] - 'A']
                  - position[conn[nodes[i] - 'A'][j] - 'A']);
          if (dist > bandwidth) {
            bandwidth = dist;
          }
        }
      }
      if (bandwidth < min_bandwidth) {
        min_bandwidth = bandwidth;
        ordering.assign(nodes);
      }
    } while (std::next_permutation(nodes.begin(), nodes.end()));

    // Output
    for (const char& ch : ordering) {
      std::cout << ch << " ";
    }
    std::cout << "-> " << min_bandwidth << std::endl;
  }

  return 0;
}

// 135 - No Rectangles

#include <iostream>

int main(int argc, char* argv[]) {

  for (int k = 0, first = 1; std::cin >> k; first = 0) {
    if (!first) { std::cout << std::endl; }

    int m = k - 1;
    for (int i = 0; i < k; ++i) {
      std::cout << 1;
      for (int j = 1; j < k; ++j) {
        std::cout << " " << i * m + j + 1;
      }
      std::cout << std::endl;
    }

    for (int i = 0; i < m; ++i) {
      for (int j = 0; j < m; ++j) {
        std::cout << i + 2;
        for (int s = 0; s < m; ++s) {
          std::cout << " " << (j + (s * i)) % m + s * m + k + 1;
        }
        std::cout << std::endl;
      }
    }

  }

  return 0;
}

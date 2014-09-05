// 138 - Street Numbers

#include <iomanip>
#include <iostream>

int main(int argc, char* argv[]) {
  for (int r0 = 0, r1 = 1, x1 = 1, found = 0; found < 10;) {
    int r2 = 6 * r1 - r0;
    int x2 = r2 + r1 + x1;
    std::cout << std::setw(10) << r2 << std::setw(10) << x2 << std::endl;
    ++found;
    r0 = r1;
    r1 = r2;
    x1 = x2;
  }
  return 0;
}

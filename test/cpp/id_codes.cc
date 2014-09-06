// 146 ID Codes

// 14158311 146 ID Codes  Accepted  C++11 0.009 2014-09-06 14:38:30

#include <iostream>
#include <algorithm>
#include <string>

int main(int argc, char* argv[]) {
  std::string code;
  while (std::cin >> code && code != "#") {
    if (std::next_permutation(code.begin(), code.end())) {
      std::cout << code << std::endl;
    } else {
      std::cout << "No Successor" << std::endl;
    }
  }
  return 0;
}

// 153 - Permalex

// Math

#include <algorithm>
#include <array>
#include <functional>
#include <iomanip>
#include <iostream>
#include <string>

namespace {

int Compute(const std::string& str) {
  std::function<long long(int)> prod = [&prod](int k) -> long long {
    return k <= 1 ? 1 : k * prod(k - 1);
  };

  std::array<int, 26> freq;
  freq.fill(0);
  std::for_each(str.begin(), str.end(), [&freq](const char& ch) {
    ++freq[ch - 'a'];
  });

  int precedes = 0;
  for (int pos = 0; pos < static_cast<int>(str.length()); ++pos) {
    // Compute #of permutations starting from pos + 1.
    long long p = prod(str.length() - pos);
    std::for_each(freq.begin(), freq.end(), [&](const int& c) {
      p /= prod(c);
    });

    std::for_each(freq.begin(), freq.begin() + str.at(pos) - 'a',
                  [&](const int& c) {
                    precedes += p * c / (str.length() - pos);
                  });

    --freq[str.at(pos) - 'a'];
  }

  return precedes + 1;
}

}  // namespace

int main(int argc, char* argv[]) {
  for (std::string str; std::getline(std::cin, str) && str != "#";) {
    std::cout << std::setw(10) << Compute(str) << std::endl;
  }

  return 0;
}

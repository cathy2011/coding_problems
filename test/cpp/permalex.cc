// 153 - Permalex

// Math

// 14215110 153 Permalex  Accepted  C++11 0.016 2014-09-16 23:45:38

#include <algorithm>
#include <array>
#include <functional>
#include <iomanip>
#include <iostream>
#include <list>
#include <numeric>
#include <string>

namespace {

void Add(std::list<int>* check, std::list<int>* to, const std::list<int>& vec) {
  std::function<int(int, int)> gcd = [&gcd](int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
  };

  for (int value : vec) {
    // For each value in 'vec', make sure it's prime relative to every element
    // in 'check' before inserting it into 'to'.
    for (auto it = check->begin(); it != check->end(); ++it) {
      int tmp = gcd(*it, value);
      *it /= tmp;
      value /= tmp;
    }
    if (value > 1) {
      to->push_back(value);
    }
  }
}

int Compute(const std::string& str) {
  // Calculate frequency of characters in the given string.
  std::array<int, 26> freq;
  freq.fill(0);
  std::for_each(str.begin(), str.end(), [&freq](const char& ch) {
    ++freq[ch - 'a'];
  });

  int precedes = 0;
  for (int pos = 0; pos < static_cast<int>(str.length()) - 1; ++pos) {
    // Compute #of permutations starting from pos + 1.
    std::list<int> up(static_cast<int>(str.length()) - 1 - pos), down;
    std::iota(up.begin(), up.end(), 1);
    std::for_each(freq.begin(), freq.end(), [&](const int& c) {
                    // Add 1...c to 'down'
                    std::list<int> tmp(c);
                    std::iota(tmp.begin(), tmp.end(), 1);
                    Add(&up, &down, tmp);
                  });
    long long up_prod = std::accumulate(up.begin(), up.end(), 1,
                                        std::multiplies<long long>());
    long long down_prod = std::accumulate(down.begin(), down.end(), 1,
                                          std::multiplies<long long>());

    std::for_each(freq.begin(), freq.begin() + str.at(pos) - 'a',
                  [&](const int& c) {
                    precedes += up_prod * c / down_prod;
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

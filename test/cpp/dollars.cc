// 147 - Dollars
// Dynamic Programming

// 14160832 147 Dollars Accepted  C++11 1.625 2014-09-07 02:26:10

#include <cmath>
#include <iomanip>
#include <iostream>
#include <map>
#include <tuple>
#include <sstream>

namespace {

static const int COINS[] = {
    5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000,
};

typedef long long MyInt;

static std::map<std::tuple<int, int>, MyInt> cache;

// Calculate the number of ways to change 'amount' using coins
// up to COINS[upto].
MyInt Change(int amount, int upto) {
  if (upto == 0) { return 1; }

  // Check our cache.
  auto it = cache.find(std::make_tuple(amount, upto));
  if (it != cache.end()) {  // Cache hit!
    return it->second;
  }

  MyInt count = 0;
  for (int k = 0; k <= amount / COINS[upto]; ++k) {
    // Use k of such coins.
    count += Change(amount - k * COINS[upto], upto - 1);
  }

  // Cache the result before returning.
  cache.emplace(std::make_tuple(amount, upto), count);

  return count;
}

}

int main(int argc, char* argv[]) {
  for (double amount; std::cin >> amount && amount; ) {
    int int_amount = static_cast<int>(std::round(amount * 100.00));
    MyInt num_of_ways = Change(int_amount, sizeof(COINS) / sizeof(int) - 1);
    std::ostringstream oss;
    oss << int_amount / 100 << "."
        << std::setw(2) << std::setfill('0') << int_amount % 100;
    std::cout << std::setw(6) << oss.str()
        << std::setw(17) << std::setfill(' ') << num_of_ways
        << std::endl;
  }
  return 0;
}

// 136 - Ugly Numbers

#include <iostream>
#include <algorithm>

int main(int argc, char* argv[]) {
  const int TARGET = 1500;

  int ugly_numbers[TARGET + 1];
  ugly_numbers[1] = 1;

  int current[] = {1, 1, 1};
  const int factors[] = {2, 3, 5};

  for (int n = 2; n <= TARGET; ++n) {
    ugly_numbers[n] = std::min({ugly_numbers[current[0]] * factors[0],
                                ugly_numbers[current[1]] * factors[1],
                                ugly_numbers[current[2]] * factors[2]});
    for (int i = 0; i < 3; ++i) {
      if (ugly_numbers[n] == ugly_numbers[current[i]] * factors[i]) {
        ++current[i];
      }
    }
  }

  std::cout << "The 1500'th ugly number is " << ugly_numbers[TARGET] << "." << std::endl;

  return 0;
}

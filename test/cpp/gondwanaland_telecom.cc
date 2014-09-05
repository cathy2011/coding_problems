// 145 - Gondwanaland Telecom

// 14148970 145 Gondwanaland Telecom  Accepted  C++11 0.012 2014-09-05 02:15:10

#include <iomanip>
#include <iostream>
#include <string>

static const int TOTAL_MINUTES = 1440;

enum Period {DAY = 0, EVENING = 1, NIGHT = 2};

static const double RATES[][3] = {
    {0.10, 0.06, 0.02},
    {0.25, 0.15, 0.05},
    {0.53, 0.33, 0.13},
    {0.87, 0.47, 0.17},
    {1.44, 0.80, 0.30},
};

#define SLOT(h, m) ((h * 60 + m) % TOTAL_MINUTES)

int main(int argc, char* argv[]) {

  Period arr[TOTAL_MINUTES];

  for (int i = SLOT(8, 0), j = SLOT(18, 0); i != j; i = (i + 1) % TOTAL_MINUTES) {
    arr[i] = DAY;
  }
  for (int i = SLOT(18, 0), j = SLOT(22, 0); i != j; i = (i + 1) % TOTAL_MINUTES) {
      arr[i] = EVENING;
  }
  for (int i = SLOT(22, 0), j = SLOT(8, 0); i != j; i = (i + 1) % TOTAL_MINUTES) {
    arr[i] = NIGHT;
  }

  char step;
  std::string number;
  int s_hour, s_min, e_hour, e_min;
  while (std::cin >> step && step != '#') {
    std::cin >> number >> s_hour >> s_min >> e_hour >> e_min;
    int p[3] = {0, 0, 0};
    for (int i = SLOT(s_hour, s_min), j = SLOT(e_hour, e_min), first = 1;
         first || i != j;
         i = (i + 1) % TOTAL_MINUTES, first = 0) {
      ++p[arr[i]];
    }
    std::cout << std::setw(10) << number
              << std::setw(6) << p[DAY]
              << std::setw(6) << p[EVENING]
              << std::setw(6) << p[NIGHT]
              << std::setw(3) << step
              << std::setw(8) << std::setprecision(2) << std::fixed
              << p[DAY] * RATES[step - 'A'][DAY]
                      + p[EVENING] * RATES[step - 'A'][EVENING]
                      + p[NIGHT] * RATES[step - 'A'][NIGHT]
              << std::endl;
  }

  return 0;
}


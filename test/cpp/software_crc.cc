// 128 - Software CRC
// 14124993 128 Software CRC  Accepted  C++11 0.615 2014-08-31 17:20:10

#include <iostream>
#include <cstring>
#include <cstdio>

static const int MOD_G = 34943;

// Treat a string as 256-based number and calculate the result
// of the string % MOD_G.
static int Mod(char* str, int len) {
  int current_pos = 0;
  int reminder = 0;
  while (current_pos < len) {
    reminder = (reminder * 256 + int(str[current_pos])) % MOD_G;
    ++current_pos;
  }
  return reminder;
}

// Output.
static void Output(int number) {
  printf("%02X %02X\n", (number & 0xFF00) >> 8, number & 0xFF);
}

int main(int argc, char* argv[]) {

  char str[1024 + 3];
  int len;
  while (std::cin.getline(str, 1024)
      && ((len = strlen(str)) == 0 || str[0] != '#')) {
    str[len] = str[len + 1] = str[len + 2] = '\0';
    int reminder = Mod(str, len + 2);
    Output(reminder == 0 ? 0 : MOD_G - reminder);
  }

  return 0;
}

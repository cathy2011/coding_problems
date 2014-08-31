// 129 - Krypton Factor
// 14125583 129 Krypton Factor  Accepted  C++11 0.013 2014-08-31 18:58:15

#include <iostream>
#include <string>

namespace {

// n in [0, 26).
inline char GetNthChar(int n) {
  return char('A' + n);
}

// Given a hard string, check whether a character can be appended generating
// a new hard string.
bool CanAppend(const std::string& str, char ch) {
  std::string tmp = str + ch;
  for (int pos = tmp.length() - 1; pos >= 0; --pos) {
    int len = tmp.length() - pos;
    if (pos >= len && tmp.substr(pos - len, len) == tmp.substr(pos)) {
      return false;
    }
  }
  return true;
}

// Find the rank'th hard string relative to 'current', which itself is
// a hard string.
bool Find(const std::string& current, int* rank, int chars,
    std::string* result) {
  if (*rank == 0) {
    result->assign(current);
    return true;
  }
  // rank > 0
  --(*rank);
  for (int i = 0; i < chars; ++i) {
    if (CanAppend(current, GetNthChar(i)) &&
        Find(current + GetNthChar(i), rank, chars, result)) {
      return true;
    }
  }
  return false;
}

void Output(const std::string& result) {
  for (int i = 0; i < result.length(); ++i) {
    if (i > 0) {
      if (i % (16 * 4) == 0) {
        std::cout << std::endl;
      } else if (i % 4 == 0) {
        std::cout << " ";
      }
    }
    std::cout << result[i];
  }
  std::cout << std::endl << result.length() << std::endl;
}

}

int main(int argc, char* argv[]) {

  int rank, chars;
  while (std::cin >> rank >> chars && rank > 0 && chars > 0) {
    std::string result;
    Find("", &rank, chars, &result);
    Output(result);
  }

  return 0;
}

// 134 - Loglan-A Logical Language

#include <iostream>
#include <string>
#include <vector>

namespace {

enum Gram {
  A,
  MOD,
  LA,
  BA,
  DA,
  PREDA,
  NAM,
  SE,
  PC,
  P,
  PN,
  PS,
  ST,
  VP,
  PV,
  UN
};

const bool IS_VOWEL[] = { 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0,
    0, 0, 1, 0, 0, 0, 0, 0 };

const Gram TRANSITION[][4] = {
    { PREDA, UN, PREDA, PREDA }, { PREDA, UN, UN, PS }, { NAM, UN, UN, PN },
    { LA, UN, PS, PN }, { MOD, UN, PS, VP }, { A, PS, PS, PS },
    { PS, UN, UN, P }, { DA, UN, P, PC }, { BA, PN, P, PC },
    { VP, PN, UN, PV }, { PV, UN, PN, ST }, { PV, UN, UN, ST },
    { PC, UN, UN, SE }, { ST, UN, UN, SE }, };

Gram TokenToGram(const std::string& token) {
  if (token.back() < 'a' || token.back() > 'z'
      || !IS_VOWEL[token.back() - 'a']) {
    return NAM;
  }
  if (token.length() == 1) {
    return A;
  } else if (token.length() == 5) {
    int tmp = 0;
    for (int i = 0; i < 5; ++i) {
      tmp = (tmp << 1) | IS_VOWEL[token[i] - 'a'];
    }
    return tmp == 5 || tmp == 9 ? PREDA : UN;
  } else if (token.length() == 2) {
    switch (token[0]) {
      case 'g':
        return MOD;
      case 'b':
        return BA;
      case 'd':
        return DA;
      case 'l':
        return LA;
      default:
        return UN;
    }
  }
  return UN;
}

// Check whether a string is a valid sentence.
bool Valid(std::vector<Gram>* tokens) {
  for (int i = 0; i < 14; ++i) {
    const Gram* t = TRANSITION[i];  // shorthand
    for (auto j = tokens->begin(); j != tokens->end();) {
      if (*j != t[0]) {
        ++j;
        continue;
      }
      if (t[1] != UN && (j == tokens->begin() || *(j - 1) != t[1])) {
        ++j;
        continue;
      }
      if (t[2] != UN && (j == tokens->end() - 1 || *(j + 1) != t[2])) {
        ++j;
        continue;
      }
      j = t[1] != UN ? tokens->erase(j - 1) : j;
      j = t[2] != UN ? tokens->erase(j + 1) - 1 : j;
      *j = t[3];
    }
  }
  return tokens->size() == 1 && tokens->front() == SE;
}

}  // namespace

int main(int argc, char* argv[]) {

  std::vector<Gram> tokens;
  for (std::string token; std::cin >> token && token != "#";) {
    if (token.back() != '.') {
      tokens.push_back(TokenToGram(token));
    } else {  // The last token for the current input case.
      if (token.length() > 1) {
        std::string tmp = token.substr(0, token.length() - 1);
        tokens.push_back(TokenToGram(tmp));
      }
      std::cout << (Valid(&tokens) ? "Good" : "Bad!") << std::endl;
      tokens.clear();
    }
  }

  return 0;
}

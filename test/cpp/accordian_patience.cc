// UVA 127 - "Accordian" Patience
// 14124694 127 "Accordian" Patience  Accepted  C++11 0.699 2014-08-31 16:18:00

#include <iostream>
#include <algorithm>
#include <string>
#include <vector>
#include <functional>

// Each card pile contains at most 52 cards. 'head' points the top card
// while 'size' specifies how many cards the pile contains.
struct CardPile {

  CardPile(char suit, char value)
      : head(0), size(1) {
    cards[0][0] = value;
    cards[0][1] = suit;
  }

  char cards[52][2];
  int head;
  int size;

};

int main(int argc, char* argv[]) {
  std::string line;
  while (std::getline(std::cin, line) && line[0] != '#') {
    std::vector<CardPile> piles;

    // Read in 52 cards.
    std::string line2;
    std::getline(std::cin, line2);
    line.append(line2);
    char suit = '\0', value = '\0';
    for (int i = 0; i < line.length(); ++i) {
      if (line[i] != ' ') {
        if (value == '\0') {
          value = line[i];
        } else if (suit == '\0') {
          suit = line[i];
        }
        if (value != '\0' && suit != '\0') {
          piles.emplace_back(suit, value);
          suit = '\0', value = '\0';
        }
      }
    }

    auto equals = [](const CardPile& p1, const CardPile& p2) {
      for (int i = 0; i < 2; ++i) {
        if (p1.cards[p1.head][i] == p2.cards[p2.head][i]) return true;
      }
      return false;
    };

    std::function<void(CardPile*, CardPile*)> move_card =
        [](CardPile* from, CardPile* to) {
          to->head = (to->head - 1 + 52) % 52;
          to->cards[to->head][0] = from->cards[from->head][0];
          to->cards[to->head][1] = from->cards[from->head][1];
          ++to->size;

          --from->size;
          from->head = (from->head + 1) % 52;
        };

    int pos = 0;
    while (pos < piles.size()) {
      while (true) {
        int pos_to_check = -1;
        if (pos >= 3 && equals(piles[pos - 3], piles[pos])) {
          move_card(&piles[pos], &piles[pos - 3]);
          pos_to_check = pos;
          pos = pos - 3;
        } else if (pos >= 1 && equals(piles[pos - 1], piles[pos])) {
          move_card(&piles[pos], &piles[pos - 1]);
          pos_to_check = pos;
          pos = pos - 1;
        }
        if (pos_to_check >= 0) {
          if (piles[pos_to_check].size <= 0) {
            piles.erase(piles.begin() + pos_to_check);
          }
        } else {
          break;  // At a dead position now.
        }
      }
      ++pos;
    }

    // output
    std::cout << piles.size() << " " << (piles.size() > 1 ? "piles" : "pile")
        << " remaining:";
    std::for_each(piles.begin(), piles.end(),
        [](const CardPile& pile) {std::cout << " " << pile.size;});
    std::cout << std::endl;

  }
  return 0;
}

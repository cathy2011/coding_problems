// 131 - The Psychic Poker Player
// 14131066 131 The Psychic Poker Player  Accepted  C++11 0.019 2014-09-01 20:25:31

#include <algorithm>
#include <array>
#include <functional>
#include <iostream>
#include <iterator>
#include <stack>
#include <string>
#include <vector>

namespace {

static const char* POKER_STR[] = { "straight-flush", "four-of-a-kind",
    "full-house", "flush", "straight", "three-of-a-kind", "two-pairs",
    "one-pair", "highest-card" };

enum Rank {
  STRAIGHT_FLUSH = 0,
  FOUR_OF_A_KIND = 1,
  FULL_HOUSE = 2,
  FLUSH = 3,
  STRAIGHT = 4,
  THREE_OF_A_KIND = 5,
  TWO_PAIRS = 6,
  ONE_PAIR = 7,
  HIGHEST_CARD = 8,
};

typedef std::string Card;

int GetCardFaceValue(const Card& card) {
  const char value = card[0];
  if (value >= '2' && value <= '9') {
    return int(value - '0');
  } else if (value == 'T') {
    return 10;
  } else if (value == 'J') {
    return 11;
  } else if (value == 'Q') {
    return 12;
  } else if (value == 'K') {
    return 13;
  } else if (value == 'A') {
    return 1;
  }
  return -1;
}

// Check whether all 5 cards of a hand have the same suit.
bool OfSameSuit(const Card hand[5]) {
  std::function<bool(const Card&)> pred =
      [hand](const Card& card) {return card[1] == hand[0][1];};
  return 5 == std::count_if(hand, hand + 5, pred);
}

// Check whether a hand is a straight hand.
bool IsStraightHand(const Card hand[5]) {
  std::array<Card, 5> the_hand;
  std::copy(hand, hand + 5, std::begin(the_hand));
  // Sort the cards by face value.
  std::sort(
      std::begin(the_hand),
      std::end(the_hand),
      [](const Card& c1, const Card& c2) {return GetCardFaceValue(c1) < GetCardFaceValue(c2);});
  std::array<Card, 5>::iterator first =
      std::adjacent_find(
          std::begin(the_hand),
          std::end(the_hand),
          [](const Card& c1, const Card& c2) {return GetCardFaceValue(c2) - GetCardFaceValue(c1) != 1;});
  if (first == the_hand.end()) {  // Not found.
    return true;
  }
  // Handle the special case where 'A' is used as the highest card.
  return the_hand[0][0] == 'A' && the_hand[1][0] == 'T'
      && std::adjacent_find(
          std::begin(the_hand) + 1,
          std::end(the_hand),
          [](const Card& c1, const Card& c2) {return GetCardFaceValue(c2) - GetCardFaceValue(c1) != 1;})
          == the_hand.end();
}

// Return the rank of a hand.
Rank GetRank(const Card hand[5]) {
  bool same_suit = OfSameSuit(hand);
  bool straight = IsStraightHand(hand);
  if (same_suit) {
    return straight ? STRAIGHT_FLUSH : FLUSH;
  }

  const std::array<int, 2> four_of_a_kind = { 4, 1 };
  const std::array<int, 2> full_house = { 3, 2 };
  const std::array<int, 3> three_of_a_kind = { 3, 1, 1 };
  const std::array<int, 3> two_pairs = { 2, 2, 1 };
  const std::array<int, 4> one_pair = { 2, 1, 1, 1 };

  std::array<Card, 5> the_hand;
  std::copy(hand, hand + 5, std::begin(the_hand));
  // Sort the cards by face value in ascending order.
  std::sort(
      std::begin(the_hand),
      std::end(the_hand),
      [](const Card& c1, const Card& c2) {return GetCardFaceValue(c1) < GetCardFaceValue(c2);});
  std::vector<int> tmp;
  Card last = "";
  int count = 0;
  for (const Card& c : the_hand) {
    // CAUTION: don't insert the first count of 0!
    if (last == "" || c[0] != last[0]) {
      if (count > 0) tmp.emplace_back(count);
      last = c;
      count = 1;
    } else {
      ++count;
    }
  }
  tmp.emplace_back(count);
  std::sort(tmp.begin(), tmp.end(), [](int a, int b) {return a > b;});

  if (tmp.size() == four_of_a_kind.size()
      && std::mismatch(std::begin(four_of_a_kind), std::end(four_of_a_kind),
                       tmp.begin()).first == std::end(four_of_a_kind)) {
    return FOUR_OF_A_KIND;
  }
  if (tmp.size() == full_house.size()
      && std::mismatch(std::begin(full_house), std::end(full_house),
                       tmp.begin()).first == std::end(full_house)) {
    return FULL_HOUSE;
  }
  if (tmp.size() == three_of_a_kind.size()
      && std::mismatch(std::begin(three_of_a_kind), std::end(three_of_a_kind),
                       tmp.begin()).first == std::end(three_of_a_kind)) {
    return THREE_OF_A_KIND;
  }
  if (tmp.size() == two_pairs.size()
      && std::mismatch(std::begin(two_pairs), std::end(two_pairs), tmp.begin()).first
          == std::end(two_pairs)) {
    return TWO_PAIRS;
  }
  if (tmp.size() == one_pair.size()
      && std::mismatch(std::begin(one_pair), std::end(one_pair), tmp.begin()).first
          == std::end(one_pair)) {
    return ONE_PAIR;
  }
  return straight ? STRAIGHT : HIGHEST_CARD;
}

Rank FindHighestRank(Card hand[], int upto, std::stack<Card>* pile) {
  Rank highest = GetRank(hand);
  if (pile->empty() || upto == 0) {
    return highest;
  }

  Card top = pile->top();
  pile->pop();
  // Replace the cards which were originally in hand.
  for (int i = 0; i < upto; ++i) {
    Card backup = hand[i];
    hand[i] = top;
    std::swap(hand[i], hand[upto - 1]);
    Rank tmp = FindHighestRank(hand, upto - 1, pile);
    if (tmp < highest) {
      highest = tmp;
    }
    std::swap(hand[i], hand[upto - 1]);
    hand[i] = backup;
  }
  pile->push(top);
  return highest;
}

}  // namespace

int main(int argc, char* argv[]) {

  Card first;
  while (std::cin >> first) {
    Card hand[5];
    hand[0] = first;
    for (int i = 0; i < 4; ++i) {
      std::cin >> hand[i + 1];
    }
    std::stack<Card> pile;
    Card tmp_pile[5];
    for (int i = 0; i < 5; ++i) {
      std::cin >> first;
      tmp_pile[i] = first;
    }
    // top of pile ---> bottom of pile.
    for (int i = 4; i >= 0; --i) {
      pile.push(tmp_pile[i]);
    }

    // Output
    std::ostream_iterator<Card> out_it(std::cout, " ");
    std::cout << "Hand: ";
    std::copy(hand, hand + 5, out_it);
    std::cout << "Deck: ";
    std::copy(tmp_pile, tmp_pile + 5, out_it);
    std::cout << "Best hand: " << POKER_STR[FindHighestRank(hand, 5, &pile)]
        << std::endl;
  }

  return 0;
}

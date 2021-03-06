// 148 - Anagram checker

// Search

// 14169386 148 Anagram checker Accepted  C++11 0.142 2014-09-08 23:00:58

#include <algorithm>
#include <array>
#include <functional>
#include <iostream>
#include <iterator>
#include <sstream>
#include <string>
#include <vector>

namespace {

// Stores a dictionary and, for a given phrase, outputs all its anagrams
// formed from words in the dictionary.
class AnagramChecker {
 public:
  // Take the only parameter of words for the dictionary.
  AnagramChecker(const std::vector<std::string>& words);

  // Return all anagrams for the given phrase.
  std::vector<std::string> Check(const std::string& phrase) const;

 private:
  // Search for dictionary words starting from index 'to_check' that
  // can make up for 'remaining'. If a solution is found, save it
  // to 'results'.
  void Search(std::array<int, 26>* remaining, int to_check,
              std::vector<std::string>* result,
              std::vector<std::vector<std::string>>* results) const;

  // Two different representations of a string word.
  std::vector<std::string> dictionary_;
  std::vector<std::array<int, 26>> dictionary_2_;

  // The transform function that can transform a word in the string
  // form to the second form, i.e. the array form.
  std::function<std::array<int, 26>(const std::string&)> transform_func_;

  // The tokenizer function that can be called to return all words in
  // a given string as a vector.
  std::function<std::vector<std::string>(const std::string&)> tokenizer_;
};

AnagramChecker::AnagramChecker(const std::vector<std::string>& words)
    : dictionary_(words) {
  transform_func_ = [](const std::string& word) {
    std::array<int, 26> word_2;
    word_2.fill(0);
    for (const char& c : word) {
      if (c >= 'A' && c <= 'Z') {
        ++word_2[static_cast<int>(c - 'A')];
      }
    }
    return word_2;
  };
  tokenizer_ = [](const std::string& phrase) {
    std::vector<std::string> words;
    std::string word;
    std::istringstream iss(phrase);
    while (iss >> word) {
      words.push_back(word);
    }
    std::sort(words.begin(), words.end());
    return words;
  };
  std::sort(dictionary_.begin(), dictionary_.end());
  std::transform(dictionary_.begin(), dictionary_.end(),
                 std::back_inserter(dictionary_2_), transform_func_);

}

std::vector<std::string> AnagramChecker::Check(
    const std::string& phrase) const {
  std::vector<std::string> sorted_original = tokenizer_(phrase);
  std::array<int, 26> phrase_2 = transform_func_(phrase);
  std::vector<std::string> result;
  std::vector<std::vector<std::string>> results;
  this->Search(&phrase_2, 0, &result, &results);

  std::vector<std::string> ret;  // for storing all found anagrams.
  for (auto& result : results) {
    std::sort(result.begin(), result.end());  // Should be unnecessary.

    // Do not output 'result' if it is the same as the original phrase.
    if (std::equal(sorted_original.begin(), sorted_original.end(),
                   result.begin())) {
      continue;
    }

    std::string str;
    for (const auto& word : result) {
      if (!str.empty()) {
        str.append(1, ' ');
      }
      str.append(word);
    }
    ret.emplace_back(str);
  }
  return ret;
}

void AnagramChecker::Search(
    std::array<int, 26>* remaining, int to_check,
    std::vector<std::string>* result,
    std::vector<std::vector<std::string>>* results) const {
  if (std::all_of(remaining->begin(), remaining->end(),
                  std::bind(std::equal_to<int>(), std::placeholders::_1, 0))) {
    // A solution has been found!
    results->emplace_back(*result);
    return;
  }

  if (to_check >= (int) dictionary_.size()) {
    return;
  }

  // Try to take the word at 'to_check'.
  if (std::mismatch(remaining->begin(), remaining->end(),
                    dictionary_2_[to_check].begin(), std::greater_equal<int>())
      .first == remaining->end()) {
    std::array<int, 26> copy_of_remaining(*remaining);
    for (int j = 0; j < 26; ++j) {
      (*remaining)[j] -= dictionary_2_[to_check][j];
    }
    result->emplace_back(dictionary_[to_check]);

    Search(remaining, to_check + 1, result, results);

    result->pop_back();
    *remaining = copy_of_remaining;
  }

  // Or not take it.
  Search(remaining, to_check + 1, result, results);
}

}

int main(int argc, char* argv[]) {

  // Read all words.
  std::vector<std::string> words;
  std::string word;
  while (std::getline(std::cin, word) && word != "#") {
    words.push_back(word);
  }

  AnagramChecker checker(words);

  // Process all phases
  while (std::getline(std::cin, word) && word != "#") {
    std::vector<std::string> results = checker.Check(word);
    for (const auto& result : results) {
      std::cout << word << " = " << result << std::endl;
    }
  }

  return 0;
}

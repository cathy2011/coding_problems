// 139 - Telephone Tangles
// Give up on this one. Likely some tricky inputs fail this code.

#include <algorithm>
#include <cstdlib>
#include <iomanip>
#include <iostream>
#include <iterator>
#include <string>

namespace {

// Organize all given destinations in a tries tree.
struct Node {
  Node() {
    std::fill(std::begin(next), std::end(next), nullptr);
    rate = -1;  // Rate invalid.
    real = false;
  }

  std::string destination;
  int rate;
  Node* next[10];
  bool real;
};

void Add(Node* node, const std::string& path, unsigned pos,
         const std::string& dest, int rate) {
  if (pos >= path.length()) {
    // 'node' is the node which should contain these information.
    node->destination.assign(dest);
    node->rate = rate;
    node->real = true;
    return;
  }
  // Consume path[pos].
  if (node->next[path[pos] - '0'] == nullptr) {
    node->next[path[pos] - '0'] = new Node;
  }
  Add(node->next[path[pos] - '0'], path, pos + 1, dest, rate);
}

// Consume the string as much as possible.
const Node* Find(Node* node, const std::string& path, unsigned* pos) {
  if (*pos >= path.length() || node->next[path[*pos] - '0'] == nullptr) {
    return node;  // We have to stop here.
  }
  int next = path[*pos] - '0';
  ++(*pos);
  return Find(node->next[next], path, pos);
}

void Delete(Node* node) {
  for (int i = 0; i < 10; ++i) {
    if (node->next[i] != nullptr) {
      Delete(node->next[i]);
      node->next[i] = nullptr;
    }
  }
  delete node;
}

bool Valid(const std::string& code) {
  for (unsigned i = 0; i < code.length(); ++i) {
    if (code[i] < '0' || code[i] > '9') {
      return false;
    }
  }
  return true;
}

}  // namespace

int main(int agrc, char* argv[]) {
  Node* root = new Node;

  std::string token;
  while (std::getline(std::cin, token) && token != "000000") {
    int space_pos = token.find_first_of(' ');
    std::string path(token, 0, space_pos);
    int dollar_pos = token.find_last_of('$');
    std::string dest(token, space_pos + 1, dollar_pos - space_pos - 1);
    int rate = std::atoi(token.substr(dollar_pos + 1).c_str());

    if (Valid(path)) {
      Add(root, path, 0, dest, rate);
    }
  }

  while (std::cin >> token && token != "#") {
    int minutes;
    std::cin >> minutes;

    if (!Valid(token) || token[0] != '0') {  // local
      std::cout << std::setw(16) << std::left << token << std::setw(5)
                << std::left << "Local" << std::setw(30) << std::right << token
                << std::setw(5) << minutes << std::setw(6) << "0.00"
                << std::setw(7) << "0.00" << std::endl;
    } else {
      unsigned pos = 0;
      const Node* n = Find(root, token, &pos);
      if (n->real
          && ((token[1] == '0' && pos >= 3 && pos <= 5
              && token.length() - pos >= 4 && token.length() - pos <= 10)
              || (token[1] != '0' && pos >= 2 && pos <= 6
                  && token.length() - pos >= 4 && token.length() - pos <= 7))) {
        std::cout << std::setw(16) << std::left << token << std::setw(25)
                  << std::left << n->destination << std::setw(10) << std::right
                  << token.substr(pos) << std::setw(5) << minutes
                  << std::setw(6) << std::setprecision(2) << std::fixed
                  << n->rate / 100.00 << std::setw(7) << std::setprecision(2)
                  << std::fixed << minutes * n->rate / 100.00 << std::endl;
      } else {
        std::cout << std::setw(16) << std::left << token << std::setw(25)
                  << std::left << "Unknown" << std::setw(10) << std::right << ""
                  << std::setw(5) << minutes << std::setw(6) << ""
                  << std::setw(7) << "-1.00" << std::endl;
      }
    }
  }

  Delete(root);

  return 0;
}

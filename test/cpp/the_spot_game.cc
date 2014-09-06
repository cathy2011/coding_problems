// 141 The Spot Game

// 14159104 141 The Spot Game Accepted  C++11 0.029 2014-09-06 17:34:47

#include <algorithm>
#include <functional>
#include <iostream>
#include <utility>
#include <vector>

// Use sparse array to save board layouts.
typedef std::pair<int, int> Position;
typedef std::vector<Position> Layout;

static int n = 0;

bool Equal(const Layout& grid_1, const Layout& grid_2) {
  if (grid_1.size() != grid_2.size()) {
    return false;
  }
  for (const Position& p : grid_1) {
    if (std::find(grid_2.begin(), grid_2.end(), p) == grid_2.end()) {
      return false;
    }
  }
  return true;
}

// Whether the given two layouts are equivalent.
bool Equivalent(const Layout& grid_1, const Layout& grid_2) {
  std::function<Layout*(Layout*)> do_nothing = [](Layout* grid) {return grid;};
  decltype(do_nothing) rotate1 = [](Layout* grid) {
    for (auto& p : *grid) {
      p = std::make_pair(p.second, n - 1 - p.first);
    }
    return grid;
  };
  decltype(do_nothing) rotate2 = [&rotate1](Layout* grid) {
    return rotate1(rotate1(grid));
  };
  decltype(do_nothing) rotate3 = [&rotate1](Layout* grid) {
    return rotate1(rotate1(rotate1(grid)));
  };
  decltype(do_nothing) flip = [](Layout* grid) {
    for (auto& p : *grid) {
      p = std::make_pair(p.first, n - 1 - p.second);
    }
    return grid;
  };

  std::vector<decltype (rotate1)> funcs = { do_nothing, rotate1, rotate2,
      rotate3, flip };
  for (auto f : funcs) {
    Layout grid_1_copy(grid_1);
    f(&grid_1_copy);
    if (Equal(grid_1_copy, grid_2)) {
      return true;
    }
  }
  return false;
}

int main(int argc, char* argv[]) {

  while (std::cin >> n && n) {
    Layout current;  // current layout.
    std::vector<Layout> past(1, current);  // stores all past layouts
    bool draw = true;

    int x, y;
    char symbol;
    for (int i = 0; i < 2 * n; ++i) {
      std::cin >> x >> y >> symbol;
      x--;
      y--;

      if (!draw) {
        continue;
      }

      if (symbol == '+') {
        current.emplace_back(x, y);
      } else if (symbol == '-') {
        current.erase(
            std::find(current.begin(), current.end(), std::make_pair(x, y)));
      }
      if (std::find_if(past.begin(), past.end(),
                       [&current](const Layout& grid) {
                         return Equivalent(current, grid);
                       }) != past.end()) {
        std::cout << "Player " << (2 - i % 2) << " wins on move " << i + 1
                  << std::endl;
        draw = false;
      } else {
        past.emplace_back(current);
      }
    }
    if (draw) {
      std::cout << "Draw" << std::endl;
    }
  }

  return 0;
}

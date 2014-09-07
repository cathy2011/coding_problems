// 142 - Mouse Clicks
// Simple Simulation.

// 14160671 142 Mouse Clicks  Accepted  C++11 0.022 2014-09-07 01:05:50

#include <iostream>
#include <iomanip>
#include <vector>
#include <limits>
#include <tuple>

namespace {

typedef std::tuple<int, int, int, int> Region;
typedef std::tuple<int, int, bool> Icon;
typedef std::tuple<int, int> Point;

bool PointInRegion(const Point& point, const Region& region) {
  int x, y, t, l, b, r;
  std::tie(x, y) = point;
  std::tie(t, l, b, r) = region;
  return x >= t && x <= b && y >= l && y <= r;
}

int DistanceBetween(const Point& from, const Point& to) {
  int x, y, z, w;
  std::tie(x, y) = from;
  std::tie(z, w) = to;
  return (w - y) * (w - y) + (z - x) * (z - x);
}

// Class Screen manages icons and regions.
class Screen {
 public:
  void AddRegion(const Region& region);
  void AddIcon(const Point& icon);
  void Click(const Point& point);

 private:
  std::vector<Region> regions_;
  std::vector<Icon> icons_;
};

void ::Screen::AddRegion(const Region& region) {
  for (auto& icon : icons_) {
    if (std::get<2>(icon)
        && PointInRegion(std::make_tuple(std::get<0>(icon), std::get<1>(icon)),
                         region)) {
      std::get<2>(icon) = false;
    }
  }
  regions_.emplace_back(region);
}

void ::Screen::AddIcon(const Point& icon) {
  bool visible = true;
  for (const auto& region : regions_) {
    if (PointInRegion(icon, region)) {
      visible = false;
      break;
    }
  }
  icons_.emplace_back(std::get<0>(icon), std::get<1>(icon), visible);
}

void ::Screen::Click(const Point& point) {
  for (int i = regions_.size() - 1; i >= 0; --i) {
    if (PointInRegion(point, regions_[i])) {  // Hit a region.
      std::cout << char('A' + i) << std::endl;
      return;
    }
  }
  std::vector<int> nearests;
  int distance = std::numeric_limits<int>::max();
  for (int i = 0; i < icons_.size(); ++i) {
    int x, y, visible;
    std::tie(x, y, visible) = icons_[i];
    if (visible) {  // Check visible icons only.
      int tmp = DistanceBetween(point, std::make_tuple(x, y));
      if (tmp < distance) {
        nearests.clear();
        distance = tmp;
        nearests.push_back(i + 1);
      } else if (tmp == distance) {
        nearests.push_back(i + 1);
      }
    }
  }
  for (const int& n : nearests) {
    std::cout << std::setw(3) << n;
  }
  std::cout << std::endl;
}

}

int main(int argc, char* argv[]) {

  Screen screen;

  while (true) {
    char type;
    std::cin >> type;
    if (type == '#') {
      break;
    }

    int x, y, u, v;
    switch (type) {
      case 'I':
        std::cin >> x >> y;
        screen.AddIcon(Point(x, y));
        break;
      case 'R':
        std::cin >> x >> y >> u >> v;
        screen.AddRegion(std::make_tuple(x, y, u, v));
        break;
      case 'M':
        std::cin >> x >> y;
        screen.Click(Point(x, y));
        break;
    }
  }

  return 0;
}


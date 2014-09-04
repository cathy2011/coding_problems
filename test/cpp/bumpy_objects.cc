// 132 - Bumpy Objects
// Not yet accepted. Got TLE.

#include <algorithm>
#include <functional>
#include <iomanip>
#include <iostream>
#include <limits>
#include <vector>

namespace {

class Point {
 public:
  Point(int id, int x, int y)
      : id(id),
        x(x),
        y(y) {
  }

  const int& get_id() const {
    return this->id;
  }

  // Calculate cross product of this and 'p', relative to 'origin'.
  int CrossProduct(const Point& origin, const Point& p) const;

  // Calculate dot product of this and 'p', relative to 'origin'.
  int DotProduct(const Point& origin, const Point& p) const;

  // Not the real distance, however.
  int DistanceTo(const Point& p) const;

  bool operator==(const Point& p) const;

  // p1 < p2 if and only if p1 is left to p2 or lower than p2.
  bool operator<(const Point& p) const;

 private:
  int id;  // Index of this point in the input.
  int x, y;
};

int ::Point::CrossProduct(const Point& origin, const Point& p) const {
  return (x - origin.x) * (p.y - origin.y) - (y - origin.y) * (p.x - origin.x);
}

int ::Point::DotProduct(const Point& origin, const Point& p) const {
  return (x - origin.x) * (p.x - origin.x) + (y - origin.y) * (p.y - origin.y);
}

int Point::DistanceTo(const Point& p) const {
  return (p.x - x) * (p.x - x) + (p.y - y) * (p.y - y);
}

bool Point::operator ==(const Point& p) const {
  return x == p.x && y == p.y;
}

bool Point::operator <(const Point& p) const {
  return x < p.x || (x == p.x && y < p.y);
}

// Calculate the convex hull for the given polygon. Note that the result convex
// hull may include colinear points.
std::vector<Point> GetConvexHull(const std::vector<Point>& polygon) {
  std::vector<Point> convex_hull;
  const Point start = *std::min_element(polygon.begin(), polygon.end());
  Point next = start;
  do {
    convex_hull.push_back(next);
    // Use 'next' as origin to find the left-most point relative to the origin.
    const Point& origin = next;  // Just a shorthand.
    auto it_next = polygon.end();
    for (auto it = polygon.begin(); it != polygon.end(); ++it) {
      if (*it == origin) {
        continue;
      }
      if (it_next == polygon.end()) {
        it_next = it;
        continue;
      }
      int cross = it->CrossProduct(origin, *it_next);
      if (cross < 0
          || (cross == 0 && it->DistanceTo(origin) < it_next->DistanceTo(origin))) {
        it_next = it;
      }
    }
    // 'it_next' now points to the next point on convex hull.
    next = *it_next;
  } while ((next == start) == false);

  convex_hull.push_back(next);  // head == tail
  return convex_hull;
}

// Get the number for the lowest numbered stable position.
int GetLowestNumberedStablePosition(const std::vector<Point>& polygon,
                                    const Point& centre) {
  std::vector<Point> convex_hull = GetConvexHull(polygon);
  auto begin = convex_hull.begin();
  auto end = begin + 1;
  int found = std::numeric_limits<int>::max();
  while (end != convex_hull.end()) {
    // Check whether segment [begin, end] can form a stable position.
    if (std::max(begin->get_id(), end->get_id()) < found
        && centre.DotProduct(*begin, *end) > 0
        && centre.DotProduct(*end, *begin) > 0) {
      found = std::max(begin->get_id(), end->get_id());
    }

    auto next = end + 1;
    if (next != convex_hull.end() && next->CrossProduct(*begin, *end) == 0) {
      // Only move 'end' if the next point sits on the line from 'begin' to 'end'.
      end = next;
    } else {
      ++begin;
      end = begin + 1;
    }
  }
  return found;
}

}  // namespace

int main(int argc, char* argv[]) {

  std::string object_name;
  while (std::cin >> object_name && object_name != "#") {
    int x = 0, y = 0, id = 0;
    std::cin >> x >> y;
    Point centre(id++, x, y);
    std::vector<Point> polygon;
    while (std::cin >> x >> y && x && y) {
      polygon.emplace_back(id++, x, y);
    }
    std::cout << std::setw(20) << std::left << object_name
              << GetLowestNumberedStablePosition(polygon, centre) << std::endl;
  }

  return 0;
}

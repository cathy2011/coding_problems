// 137 - Polygons
// 14155019 137 Polygons  Accepted  C++11 0.009 2014-09-05 23:34:09

#include <algorithm>
#include <cmath>
#include <functional>
#include <iomanip>
#include <iostream>
#include <utility>
#include <vector>

namespace {  // Some helper functions

typedef std::pair<double, double> Point;  // in clockwise.
typedef std::vector<Point> Convex;

inline double GetX(const Point& origin, const Point& a) {
  return a.first - origin.first;
}

inline double GetY(const Point& origin, const Point& a) {
  return a.second - origin.second;
}

double CrossProduct(const Point& origin, const Point& a, const Point& b) {
  return  GetX(origin, a) * GetY(origin, b) - GetY(origin, a) * GetX(origin, b);
}

double DotProduct(const Point& origin, const Point& a, const Point& b) {
  return  GetX(origin, a) * GetX(origin, b) + GetY(origin, a) * GetY(origin, b);
}

// Check whether a point lies inside or on a convex hull.
bool Contains(const Convex& convex, const Point& p) {
  for (unsigned i = 0, j = 1; i < convex.size(); ++i, j = (i + 1) % convex.size()) {
    // 'p' must lies left to segment from 'i' to 'j'.
    if (CrossProduct(convex[i], convex[j], p) > 0) {  // On right side
      return false;
    }
  }
  return true;
}

bool OnSegment(const Point& p, const Point& a, const Point& b) {
  return p.first >= std::min(a.first, b.first) &&
         p.first <= std::max(a.first, b.first) &&
         p.second >= std::min(a.second, b.second) &&
         p.second <= std::max(a.second, b.second);
}

// Return the intersection point of two segments.
bool GetIntersection(const Point& a, const Point& b, const Point& c, const Point& d,
                     Point* intersection) {
  double a_1 = b.second - a.second;
  double b_1 = a.first - b.first;
  double c_1 = a_1 * a.first + b_1 * a.second;
  double a_2 = d.second - c.second;
  double b_2 = c.first - d.first;
  double c_2 = a_2 * c.first + b_2 * c.second;

  double det = a_1 * b_2 - a_2 * b_1;
  if (std::fabs(det) < 1E-6) {
    return false;
  }
  intersection->first = (b_2 * c_1 - b_1 * c_2) / det;
  intersection->second = (a_1 * c_2 - a_2 * c_1) / det;
  return OnSegment(*intersection, a, b) && OnSegment(*intersection, c, d);
}

// Return the area of the convex hull resultant from the given set of points.
double GetArea(const std::vector<Point>& points) {
  if (points.size() < 3) { return 0.00; }

  Point start = *std::min_element(points.begin(), points.end(),
                                  [](const Point& a, const Point& b) {
    return a.first < b.first;
  });

  double area = 0.0;
  Point next = start;
  do {
    // Find the point after 'next'.
    int after = -1;
    for (unsigned i = 0; i < points.size(); ++i) {
      if (points[i] == next) { continue; }
      if (after < 0 || CrossProduct(next, points[after], points[i]) > 0) {
        after = i;
      }
    }
    if (next != start && points[after] != start) {
      area += CrossProduct(start, points[after], next);
    }
    next = points[after];
  } while (next != start);

  return area / 2.0;
}

}

int main(int argc, char* argv[]) {

  int n;
  while (std::cin >> n && n) {
    // Read two polygons.
    Convex polygon_1(n);
    for (int i = 0; i < n; ++i) {
      int x, y;
      std::cin >> x >> y;
      polygon_1[i] = std::make_pair(x, y);
    }
    std::cin >> n;
    Convex polygon_2(n);
    for (int i = 0; i < n; ++i) {
      int x, y;
      std::cin >> x >> y;
      polygon_2[i] = std::make_pair(x, y);
    }
    // Construct points for the intersection area.
    Convex intersection;
    for (const auto& p : polygon_1) {
      if (Contains(polygon_2, p)) {
        intersection.push_back(p);
      }
    }
    for (const auto& p : polygon_2) {
      if (Contains(polygon_1, p)) {
        intersection.push_back(p);
      }
    }
    for (unsigned i = 0; i < polygon_1.size(); ++i) {
      int i_to = (i + 1) % polygon_1.size();
      for (unsigned j = 0; j < polygon_2.size(); ++j) {
        int j_to = (j + 1) % polygon_2.size();
        Point p;
        if (GetIntersection(polygon_1[i], polygon_1[i_to],
                            polygon_2[j], polygon_2[j_to],
                            &p)) {
          intersection.push_back(p);
        }
      }
    }
    // Compute the area.
    std::cout << std::setw(8) << std::fixed << std::setprecision(2)
              << GetArea(polygon_1) + GetArea(polygon_2) - 2.0 * GetArea(intersection);
  }
  std::cout << std::endl;

  return 0;
}

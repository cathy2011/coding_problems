// 143 - Orchard Trees

// 14160189 143 Orchard Trees Accepted  C++11 0.129 2014-09-06 22:01:29

#include <iostream>
#include <iomanip>
#include <cmath>
#include <utility>
#include <algorithm>
#include <cassert>

namespace {  // Some helper functions.

typedef long double MyDouble;

static const MyDouble EPSON = 1e-10;

typedef std::pair<MyDouble, MyDouble> Point;

inline bool EQ(MyDouble d1, MyDouble d2) {
  return std::fabs(d1 - d2) < EPSON;
}

inline bool LE(MyDouble d1, MyDouble d2) {
  return EQ(d1, d2) || d1 < d2;
}

inline bool GE(MyDouble d1, MyDouble d2) {
  return EQ(d1, d2) || d1 > d2;
}

// The following functions make this code finally accepted.
inline MyDouble CEIL(MyDouble d1) {
  return std::ceil(d1 - EPSON);
}

inline MyDouble FLOOR(MyDouble d1) {
  return std::floor(d1 + EPSON);
}

bool OnSegment(const Point& p, const Point& a, const Point& b) {
  return GE(p.first, std::min(a.first, b.first))
      && LE(p.first, std::max(a.first, b.first))
      && GE(p.second, std::min(a.second, b.second))
      && LE(p.second, std::max(a.second, b.second));
}

// Return the intersection point of two segments.
bool GetIntersection(const Point& a, const Point& b, const Point& c,
                     const Point& d, Point* intersection) {
  MyDouble a_1 = b.second - a.second;
  MyDouble b_1 = a.first - b.first;
  MyDouble c_1 = a_1 * a.first + b_1 * a.second;
  MyDouble a_2 = d.second - c.second;
  MyDouble b_2 = c.first - d.first;
  MyDouble c_2 = a_2 * c.first + b_2 * c.second;

  MyDouble det = a_1 * b_2 - a_2 * b_1;
  if (std::fabs(det) < EPSON) {
    return false;
  }
  intersection->first = (b_2 * c_1 - b_1 * c_2) / det;
  intersection->second = (a_1 * c_2 - a_2 * c_1) / det;
  return OnSegment(*intersection, a, b) && OnSegment(*intersection, c, d);
}

int Count(const Point p[3]) {
  int count = 0;

  int from = CEIL(std::min( { p[0].first, p[1].first, p[2].first }));
  int to = FLOOR(std::max( { p[0].first, p[1].first, p[2].first }));
  for (int k = std::max(from, 1); k <= std::min(to, 99); ++k) {
    if (EQ(k, std::min( { p[0].first, p[1].first, p[2].first }))
        && EQ(k, std::max( { p[0].first, p[1].first, p[2].first }))) {
      // Three points are on the same vertical line.
      int begin = CEIL(std::min( { p[0].second, p[1].second, p[2].second }));
      begin = std::max(begin, 1);
      int end = FLOOR(std::max( { p[0].second, p[1].second, p[2].second }));
      end = std::min(end, 99);
      count += end - begin + 1;
      return count;
    }

    // There should be exactly two intersections, if any. Find them.
    Point intersections[3];
    int index = 0;
    for (int i = 0; i < 3; ++i) {
      if (GetIntersection(std::make_pair(k, -1), std::make_pair(k, 101), p[i],
                          p[(i + 1) % 3], intersections + index)) {
        ++index;
      }
    }
    // We got 'index' intersection points.
    assert(index == 0 || index == 2 || index == 3);

    if (index == 2) {
      int begin = CEIL(
          std::min(intersections[0].second, intersections[1].second));
      begin = std::max(begin, 1);
      int end = FLOOR(
          std::max(intersections[0].second, intersections[1].second));
      end = std::min(end, 99);
      if (end >= begin) {
        count += end - begin + 1;
      }
    } else if (index == 3) {
      int begin = CEIL(
          std::min( { intersections[0].second, intersections[1].second,
              intersections[2].second }));
      begin = std::max(begin, 1);
      int end = FLOOR(
          std::max( { intersections[0].second, intersections[1].second,
              intersections[2].second }));
      end = std::min(end, 99);
      if (end >= begin) {
        count += end - begin + 1;
      }
    }
  }

  return count;
}

}  // namespace

int main(int argc, char* argv[]) {

  MyDouble x, y, z, w, u, v;
  while (std::cin >> x >> y >> z >> w >> u >> v && (x || y || z || w || u || v)) {
    Point p[] = { { x, y }, { z, w }, { u, v } };
    std::cout << std::setw(4) << Count(p) << std::endl;
  }

  return 0;
}

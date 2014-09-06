// 144 Student Grants
// Simple Simulation

// 14159375 144 Student Grants  Accepted  C++11 0.012 2014-09-06 18:34:17

#include <iostream>
#include <iomanip>
#include <vector>

namespace {

static const int GRANT_MAX = 40;

class Student {
 public:
  Student()
      : got_(0) {
  }

  int Consume(int store) {
    got_ += store;
    if (got_ > GRANT_MAX) {
      int refund = got_ - GRANT_MAX;
      got_ = GRANT_MAX;
      return refund;
    }
    return 0;
  }

  bool Queued() const {
    return got_ < GRANT_MAX;
  }

 private:
  int got_;  // How many grants this student has got.
};

class ATM {
 public:
  ATM(int k)
      : limit_(k),
        next_value_(1) {
  }

  int Dispense() {
    int ret = next_value_;
    if (++next_value_ > limit_) {
      next_value_ = 1;
    }
    return ret;
  }

 private:
  const int limit_;
  int next_value_;
};

}

int main(int argc, char* argv[]) {

  for (int n, k; std::cin >> n >> k && n && k;) {
    std::vector<Student> students(n);
    ATM atm(k);

    int queued = n;
    int store = 0;
    int next = 0;
    while (queued) {  // while someone are still waiting
      if (students[next].Queued()) {
        if (store == 0) {
          store = atm.Dispense();
        }
        store = students[next].Consume(store);
        if (!students[next].Queued()) {
          --queued;
          std::cout << std::setw(3) << next + 1;
        }
      }
      next = (next + 1) % n;
    }
    std::cout << std::endl;
  }

  return 0;
}

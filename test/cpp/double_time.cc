// 150 - Double Time

// Simulation

#include <iostream>
#include <string>
#include <map>
#include <sstream>

namespace {

enum WeekDay {
  Monday = 0, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday,
  NumOfWeekdays = 7,
};

std::map<WeekDay, std::string> weekday_to_str = {
    {Monday, "Monday"},
    {Tuesday, "Tuesday"},
    {Wednesday, "Wednesday"},
    {Thursday, "Thursday"},
    {Friday, "Friday"},
    {Saturday, "Saturday"},
    {Sunday, "Sunday"},
};

std::map<std::string, WeekDay> str_to_weekday = {
    {"Monday", Monday},
    {"Tuesday", Tuesday},
    {"Wednesday", Wednesday},
    {"Thursday", Thursday},
    {"Friday", Friday},
    {"Saturday", Saturday},
    {"Sunday", Sunday},
};

enum Month {
  January = 0, February, March, April, May, June, July, August, September, October,
  November, December,
  NumOfMonths = 12,
};

std::map<Month, std::string> month_to_str = {
    {January, "January"},
    {February, "February"},
    {March, "March"},
    {April, "April"},
    {May, "May"},
    {June, "June"},
    {July, "July"},
    {August, "August"},
    {September, "September"},
    {October, "October"},
    {November, "November"},
    {December, "December"},
};

std::map<std::string, Month> str_to_month = {
    {"January", January},
    {"February", February},
    {"March", March},
    {"April", April},
    {"May", May},
    {"June", June},
    {"July", July},
    {"August", August},
    {"September", September},
    {"October", October},
    {"November", November},
    {"December", December},
};

std::map<Month, int> days_in_month_leap = {
    {January, 31},
    {February, 29},
    {March, 31},
    {April, 30},
    {May, 31},
    {June, 30},
    {July, 31},
    {August, 31},
    {September, 30},
    {October, 31},
    {November, 30},
    {December, 31},
};

std::map<Month, int> days_in_month_non_leap = {
    {January, 31},
    {February, 28},
    {March, 31},
    {April, 30},
    {May, 31},
    {June, 30},
    {July, 31},
    {August, 31},
    {September, 30},
    {October, 31},
    {November, 30},
    {December, 31},
};

class MyTime {
 public:
  MyTime(bool old, WeekDay dow, int dom, Month moy, int year)
      : old_(old), day_of_week_(dow), day_of_month_(dom), month_of_year_(moy), year_(year) {

  }

  // Prefix increment
  void operator ++() {
    day_of_week_ = (day_of_week_ + 1) % NumOfWeekdays;

    const std::map<Month, int>& m2d = IsLeap() ? days_in_month_leap : days_in_month_non_leap;
    Month month = static_cast<Month>(month_of_year_);
    if (++day_of_month_ > m2d.at(month)) {
      day_of_month_ -= m2d.at(month);
      if (++month_of_year_ >= NumOfMonths) {
        month_of_year_ -= NumOfMonths;
        ++year_;
      }
    }
  }

  bool operator ==(const MyTime& o) {
    return
        day_of_week_ == o.day_of_week_ &&
        day_of_month_ == o.day_of_month_ &&
        month_of_year_ == o.month_of_year_ &&
        year_ == o.year_;
  }

  bool operator !=(const MyTime& o) {
    return !(*this == o);
  }

  std::string ToString() const {
    std::ostringstream oss;
    oss << weekday_to_str[static_cast<WeekDay>(day_of_week_)] << " "
        << day_of_month_ << (old_ ? "*" : "") << " "
        << month_to_str[static_cast<Month>(month_of_year_)] << " "
        << year_;
    return oss.str();
  }

 private:
  bool IsLeap() {
    if (old_) {
      return year_ % 4 == 0;
    }
    return (year_ % 4 == 0 && year_ % 100 != 0) || year_ % 400 == 0;
  }

  bool old_;
  int day_of_week_;
  int day_of_month_;
  int month_of_year_;
  int year_;
};

std::ostream& operator<<(std::ostream& out, const MyTime& t) {
  return out << t.ToString();
}

}  // namespace

int main(int argc, char* argv[]) {

  std::string week, month;
  int day, year;
  while (std::cin >> week && week != "#") {
    std::cin >> day >> month >> year;
    MyTime old_day(true, Friday, 5, October, 1582);
    MyTime new_day(false, Friday, 15, October, 1582);
    MyTime target(true /* meaningless here */, str_to_weekday.at(week), day, str_to_month.at(month), year);
    while (old_day != target && new_day != target) {
      ++old_day;
      ++new_day;
    }
    std::cout << (old_day == target ? new_day : old_day) << std::endl;
  }

  return 0;
}

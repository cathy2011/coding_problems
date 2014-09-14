// 150 - Double Time, second version

// 14203691  150 Double Time Accepted  C++11 0.065 2014-09-14 21:10:18

// Simulation

#include <iostream>
#include <string>
#include <map>

namespace {

enum WeekDay {
  Monday = 0, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday,
  NumOfWeekdays = 7,
};

const std::map<WeekDay, std::string> weekday_to_str = {
    {Monday, "Monday"},
    {Tuesday, "Tuesday"},
    {Wednesday, "Wednesday"},
    {Thursday, "Thursday"},
    {Friday, "Friday"},
    {Saturday, "Saturday"},
    {Sunday, "Sunday"},
};

const std::map<std::string, WeekDay> str_to_weekday = {
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

const std::map<std::string, Month> str_to_month = {
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

enum CalendarType {
  OLD_CALENDAR = 0, NEW_CALENDAR, NumOfCalendars,
};

bool IsLeap(int year, CalendarType cal) {
  if (cal == OLD_CALENDAR) {
    return year % 4 == 0;
  }
  return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
}

const int dom[2][NumOfMonths] = {
    {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
    {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
};

int doy[NumOfCalendars][600];  // Start from 1582/10/05 and 10/15 respectively.
int acc_dom[2][NumOfMonths];

void Preprocess() {
  for (int year = 1582, diff = 0; year < 2101; ++year, ++diff) {
    for (CalendarType cal = OLD_CALENDAR; cal < NumOfCalendars; cal = static_cast<CalendarType>(cal + 1)) {
      if (year == 1582) {
        doy[cal][diff] = 0;
      } else {
        doy[cal][diff] = doy[cal][diff - 1] + (IsLeap(year, cal) ? 366 : 365);
      }
    }
  }
  for (Month m = January; m < NumOfMonths; m = static_cast<Month>(m + 1)) {
    if (m == January) {
      acc_dom[0][m] = 0;
      acc_dom[1][m] = 0;
    } else {
      acc_dom[0][m] = acc_dom[0][m - 1] + dom[0][m - 1];
      acc_dom[1][m] = acc_dom[1][m - 1] + dom[1][m - 1];
    }
  }
}

int ddd[NumOfCalendars] = {5, 15};

int GetNumOfDays(int y, Month m, int d, CalendarType cal) {
  int days = 0;
  // Align the year first.
  days += doy[cal][y - 1582];
  // Align the month.
  int is_leap = IsLeap(y, cal);
  days += acc_dom[is_leap][m] - acc_dom[is_leap][October];
  // Align the day
  days += d - ddd[cal];
  return days;
}

void GetDate(int* y, Month* m, int* d, int days, CalendarType cal) {
  int year = 0;
  while (doy[cal][year + 1] < days) { ++year; }
  days -= doy[cal][year];
  *y = 1582 + year;

  days += ddd[cal];

  int is_leap = IsLeap(*y + 1, cal);
  *m = October;
  while (days > dom[is_leap][*m]) {
    days -= dom[is_leap][*m];
    if (*m == December) {
      *y += 1;
      *m = January;
    } else {
      *m = static_cast<Month>(*m + 1);
    }
  }

  *d = days;
}


}  // namespace

int main(int argc, char* argv[]) {
  Preprocess();

  std::string week, month;
  int day, year;
  while (std::cin >> week && week != "#") {
    std::cin >> day >> month >> year;
    // Treat it as an old date.
    int days = GetNumOfDays(year, str_to_month.at(month), day, OLD_CALENDAR);
    if ((Friday + days) % NumOfWeekdays == str_to_weekday.at(week)) {  // it is an old date.
      int y, d; Month m;
      GetDate(&y, &m, &d, days, NEW_CALENDAR);
      std::cout << week << " " << d << " " << month_to_str[m] << " " << y << std::endl;
    } else {
      days = GetNumOfDays(year, str_to_month.at(month), day, NEW_CALENDAR);
      int y, d; Month m;
      GetDate(&y, &m, &d, days, OLD_CALENDAR);
      std::cout << week << " " << d << "* " << month_to_str[m] << " " << y << std::endl;
    }
  }

  return 0;
}

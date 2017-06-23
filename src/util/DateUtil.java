package util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


/**
 * This is utility class to support the date manipulate.
 */
public class DateUtil {
  /**
   * Get a LocalDate object with the given date represented by integer.
   * Generate a LocalDate object with the given date with format "YYYYMMDD".
   *
   * @param date the date in integer notation
   * @return a LocalDate object
   */
  public static LocalDate getLocalDate(int date) {
    String daystr = String.valueOf(date);
    int day = Integer.parseInt(daystr.substring(6));
    int mon = Integer.parseInt(daystr.substring(4, 6));
    int year = Integer.parseInt(daystr.substring(0, 4));
    return LocalDate.of(year, mon, day);
  }

  /**
   * Get a LocalDate object with the given date represented by integer.
   *
   * @param date the day of a certain day
   * @param mon  the month of a certain day
   * @param year the year of a certain day
   * @return a LocalDate object
   */
  public static LocalDate processDayMonYear(int date, int mon, int year) {
    return LocalDate.of(year, mon, date);
  }

  /**
   * Calculate the differences in days with given two days.
   * Example: timeDiff(20170701, 20170601) output: 30.
   *
   * @param d1 the end date
   * @param d2 the start date
   * @return the differences in days
   */
  public static int timeDiff(int d1, int d2) {
    LocalDate date1 = getLocalDate(d1);
    LocalDate date2 = getLocalDate(d2);
    return (int) ChronoUnit.DAYS.between(date2, date1);

  }

  /**
   * Returns a LocalDate object with the specified number of days subtracted.
   *
   * @param days days need to be subtracted
   * @param date the day of the certain date
   * @param mon  the month of the certain date
   * @param year the year of the certain date
   * @return a LocalDate object with the specified number of days subtracted
   */
  public static LocalDate minusDay(int days, int date, int mon, int year) {
    LocalDate enddate = processDayMonYear(date, mon, year);
    LocalDate startDate = enddate.minusDays(days);
    return startDate;
  }

  /**
   * Minus given days from given date.
   *
   * @param days days need to be subtracted
   * @param date given date
   * @return a LocalDate object with the specified number of days subtracted
   */
  public static LocalDate minusDay(int days, LocalDate date) {
    return date.minusDays(days);
  }

  /**
   * Convert a Local Date object into integer notation.
   *
   * @param date the given local date object
   * @return integer notation of given date
   */
  public static int convertInt(LocalDate date) {
    int actualDate = date.getDayOfMonth();
    int actualYear = date.getYear() % 100;
    int actualMonth = date.getMonthValue();
    if (actualYear <= LocalDate.now().getYear() % 100) {
      actualYear = LocalDate.now().getYear() / 100 * 100 + actualYear;
    } else {
      actualYear = (LocalDate.now().getYear() / 100 - 1) * 100 + actualYear;
    }
    return (actualYear * 100 + actualMonth) * 100 + actualDate;
  }


}

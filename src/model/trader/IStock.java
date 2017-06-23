package model.trader;

import java.util.Map;

import util.PriceRecord;

/**
 * This is an IStock interface contains the operation that all types of stock including
 * a stock and a basket of stocks should support.
 */
public interface IStock {

  /**
   * Look up the price of a stock or a basket of stocks on a certain day.
   * Return PriceRecord object which contains the highest/lowest/open/closing price of a stock or a
   * basket of stocks on a certain day.
   *
   * @param date the day of a certain day
   * @param mon  the month of a certain day
   * @param year the year of a certain day
   * @return a PriceRecord of a stock or a basket of stocks on a certain day
   * @throws Exception when cannot retrieve data
   */
  PriceRecord getPrice(int date, int mon, int year) throws Exception;

  /**
   * Get historical (closing) prices for a stock or a basket of stock for a certain date range.
   * With input format as DD, MM, YYYY.
   * Return a map with integer representation (YYYYMMDD) of a certain day as key, and the closing
   * price of a stock or a basket of stock on a certain day as value.
   *
   * @param fromDate the start day for a certain date range
   * @param fromMon  the start month for a certain date range
   * @param fromYear the start year for a certain date range
   * @param toDate   the end day for a certain date range
   * @param toMon    the end month for a certain date range
   * @param toYear   the end year for a certain date range
   * @return a map with date as key and closing price as value.
   * @throws Exception when cannot retrieve data
   */
  Map<Integer, Double> getHistoricalClosing(int fromDate, int fromMon, int fromYear,
                                            int toDate, int toMon, int toYear) throws Exception;

  /**
   * Get historical (closing) prices for a stock or a basket of stock for a certain date range.
   * With input format as YYYYMMDD.
   * Return a map with integer representation (YYYYMMDD) of a certain day as key, and the closing
   * price of a stock or a basket of stock on a certain day as value.
   *
   * @param fromDate the start day for a certain date range
   * @param toDate   the end day for a certain date range
   * @return a map with date as key and closing price as value.
   * @throws Exception Exception when cannot retrieve data
   */
  Map<Integer, Double> getHistoricalClosing(int fromDate, int toDate) throws Exception;

  /**
   * Determine if a stock or a basket trends up during a certain date range using given trend
   * calculator.
   * Return the string representation of the trend.
   *
   * @param fromDate        the start day for a certain date range
   * @param fromMon         the start month for a certain date range
   * @param fromYear        the start year for a certain date range
   * @param toDate          the end day for a certain date range
   * @param toMon           the end month for a certain date range
   * @param toYear          the end year for a certain date range
   * @param trendCalculator the trend calculator
   * @return the difference of closing price between the end date and start date
   * @throws Exception when cannot retrieve data
   */
  String trend(int fromDate, int fromMon, int fromYear,
               int toDate, int toMon, int toYear, TrendCalculator trendCalculator)
          throws Exception;

  /**
   * Determine if a stock or a basket trends up during a certain date range using given trend
   * calculator.
   * Return the string representation of the trend.
   *
   * @param fromDate        the start day for a certain date range
   * @param toDate          the end day for a certain date range
   * @param trendCalculator the trend calculator
   * @return the string represent of trend
   * @throws Exception when cannot retrieve data
   */
  String trend(int fromDate, int toDate, TrendCalculator trendCalculator) throws Exception;

  /**
   * Determine if there is a buying opportunity for a certain stock on a certain day.
   * The day at which the 50-day moving average crosses the 200-day moving average is often
   * regarded as a good day to buy.
   *
   * @param date the day of a certain day
   * @param mon  the month of a certain day
   * @param year the year of a certain day
   * @throws Exception when cannot retrieve data
   */
  boolean buyOpportunity(int date, int mon, int year) throws Exception;

  /**
   * Return the string representation of this IStock.
   *
   * @return the string representation of this IStock.
   */
  String toString();

  /**
   * Get X-day moving average of this basket within certain date range.
   *
   * @param fromDate from date of certain date range
   * @param toDate   to date of certain date range
   * @param days     X-days
   * @return X-day moving average
   * @throws Exception when cannot retrieve data
   */
  Map<Integer, Double> getAveRange(int fromDate, int toDate, int days) throws Exception;
}

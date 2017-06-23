package model.trader;

import java.util.Map;


/**
 * This is an trend calculator interface contains the operation that all types of trend calculator
 * should support.
 */
public interface TrendCalculator {
  /**
   * Calculate the trend by calculate the difference in price between the start and the end of
   * the date range.
   *
   * @param historicalClosing a map with date as key and closing price as value for a stock or a
   *     basket of stocks.
   * @return the trend calculate by calculating the difference in price between the start and the
   *     end of the date range
   */
  double trend(Map<Integer, Double> historicalClosing);

  /**
   * Return the String representation of trend.
   *
   * @param trend the calculated result of trend
   * @return the string representation of trend
   */
  String getTrendStatus(double trend);
}

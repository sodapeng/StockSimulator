package model.trader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * This is a class that represent the simple trend calculator.
 * Using difference of stock prices between end date and start date to determine the trend of a
 * stock or a basket of stocks prices within certain date range.
 */

public class SimpleTrendCalculator implements TrendCalculator {

  @Override
  public double trend(Map<Integer, Double> historicalClosing) {
    List sortedKeys = new ArrayList(historicalClosing.keySet());
    Collections.sort(sortedKeys);
    double fromPrice = historicalClosing.get(sortedKeys.get(0));
    double toPrice = historicalClosing.get(sortedKeys.get(sortedKeys.size() - 1));
    return toPrice - fromPrice;
  }

  /**
   * String representation of the calculated trend within certain date range
   * Return "Strong incline" if there is an increase and trend differences is greater than 45.
   * Return "Strong decline" if there is an decrease and trend differences is greater than 45.
   * Return "Gentle incline" if there is an increase and trend differences is smaller than 45.
   * Return "Strong incline" if there is an decline and trend differences is smaller than 45.
   * Return "Steady" if there is no changes on the prices.
   *
   * @param trend the calculated result of trend
   * @return the String representation of trend
   */
  @Override
  public String getTrendStatus(double trend) {
    if (trend > 45) {
      return "Strong incline";
    } else if (trend < 45 && trend > 0) {
      return "Gentle incline";
    } else if (trend < -45) {
      return "Strong decline";
    } else if (trend > -45 && trend < 0) {
      return "Gentle decline";
    } else {
      return "Steady";
    }
  }

}

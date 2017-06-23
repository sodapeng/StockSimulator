package model.trader;

import java.util.Map;

/**
 * This is a class represent the linear regression calculator model.
 * Using linear regression model to determine the trend of a  stock or a basket of stocks prices
 * within certain date range.
 */
public class LinearRegressionCalculator implements TrendCalculator {

  @Override
  public double trend(Map<Integer, Double> historicalClosing) {
    return 0.0;
  }

  @Override
  public String getTrendStatus(double trend) {
    return "";
  }
}

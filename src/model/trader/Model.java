package model.trader;

import java.util.Map;

import util.StockDataRetriever;

/**
 * This is a model interface represent the model part of this program.
 * It contains the operation that all types of trader model should support.
 */
public interface Model {

  /**
   * Retrieve and calculate the trend of an IStock with given name, date range and trend calculator.
   *
   * @param iStockName      iStock name
   * @param fromdate        from date of date range
   * @param todate          to date of date range
   * @param trendCalculator trendcalculator
   * @return trend in string notation
   * @throws Exception when cannot retrieve data
   */
  String trend(String iStockName, int fromdate, int todate, TrendCalculator trendCalculator)
          throws Exception;

  /**
   * Add Stock to existing basket.
   *
   * @param basketName  basket name
   * @param stockSymbol stock need to be added
   * @param share       share of this stock
   * @throws Exception when cannot retrieve data
   */
  void addStock(String basketName, String stockSymbol, int share) throws Exception;


  /**
   * When user create a new basket, add this empty basket to this IStock set.
   *
   * @param basketName    the name of basket
   * @param dataRetriever stockdataretriever
   */
  void add(String basketName, StockDataRetriever dataRetriever, int createDate);

  /**
   * Retrieve to X-days moving average data for given IStock name, and put them in map as result.
   * If days equal to 50, 50 days moving average will be calculated.
   * If days equal to 200, 200 days moving average will be calculated.
   * If days equals to 250, both 50 and 200 days moving average will be calculated.
   *
   * @param iStockName the name of IStock
   * @param fromDate   from date
   * @param toDate     to date
   * @param days       X-Days
   * @return result map with IStock name a key, and closing price as value
   * @throws Exception when cannot retrieve data
   */
  Map<String, Map<Integer, Double>> iStockPlotMov(String iStockName, int fromDate,
                                                  int toDate, int days) throws Exception;

  /**
   * Retrieve closing price data for given IStock name, and put them in map as result.
   *
   * @param iStockName the name of IStock
   * @param fromDate   from date
   * @param toDate     to date
   * @return result map with IStock name a key, and closing price as value
   * @throws Exception when cannot retrieve data
   */
  Map<String, Map<Integer, Double>> iStockPlot(String iStockName, int fromDate, int toDate)
          throws Exception;
}

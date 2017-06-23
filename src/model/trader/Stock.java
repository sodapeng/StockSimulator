package model.trader;

import java.util.Map;
import java.util.TreeMap;

import util.Calculate;
import util.PriceRecord;
import util.StockDataRetriever;

/**
 * This is a class represent a stock.
 */
public class Stock implements IStock {
  private String stockSymbol;
  private StockDataRetriever dataRetriever;

  /**
   * Construct a stock object.
   * Create a stock with one share (single stock) with stock symbol, and a stock data retriever.
   *
   * @param stockSymbol   the stock symbol of this stock
   * @param dataRetriever the data retriever to get data from web
   */
  public Stock(String stockSymbol, StockDataRetriever dataRetriever) throws Exception {
    this.stockSymbol = stockSymbol;
    this.dataRetriever = dataRetriever;
    isValid();
  }

  @Override
  public PriceRecord getPrice(int date, int mon, int year) throws Exception {
    return Calculate.getPrice(stockSymbol, dataRetriever, date, mon, year);
  }

  @Override
  public Map<Integer, Double> getHistoricalClosing(int fromDate, int fromMon, int fromYear,
                                                   int toDate, int toMon, int toYear) throws Exception {
    Map<Integer, PriceRecord> map = dataRetriever.getHistoricalPrices(stockSymbol,
            fromDate, fromMon, fromYear, toDate, toMon, toYear);

    if (map.isEmpty()) {
      throw new IllegalArgumentException("invalid date range");
    }

    Map<Integer, Double> stockHC = new TreeMap<Integer, Double>();

    for (Map.Entry<Integer, PriceRecord> e : map.entrySet()) {
      stockHC.put(e.getKey(), e.getValue().getClosePrice());
    }
    return stockHC;
  }

  @Override
  public Map<Integer, Double> getHistoricalClosing(int fromDate, int toDate) throws Exception {
    String fromDatestr = String.valueOf(fromDate);
    int fromday = Integer.parseInt(fromDatestr.substring(6));
    int frommon = Integer.parseInt(fromDatestr.substring(4, 6));
    int fromyear = Integer.parseInt(fromDatestr.substring(0, 4));
    String toDatestr = String.valueOf(toDate);
    int today = Integer.parseInt(toDatestr.substring(6));
    int tomon = Integer.parseInt(toDatestr.substring(4, 6));
    int toyear = Integer.parseInt(toDatestr.substring(0, 4));
    return getHistoricalClosing(fromday, frommon, fromyear, today, tomon, toyear);
  }

  @Override
  public String trend(int fromDate, int fromMon, int fromYear,
                      int toDate, int toMon, int toYear,
                      TrendCalculator trendCalculator) throws Exception {
    Map<Integer, Double> stockHC = getHistoricalClosing(fromDate, fromMon, fromYear,
            toDate, toMon, toYear);

    //Throw exception when there is less than two business day in certain day range
    if (stockHC.size() < 2) {
      throw new IllegalArgumentException("invalid date range");
    }
    double trend = trendCalculator.trend(stockHC);
    return trendCalculator.getTrendStatus(trend);
  }

  @Override
  public String trend(int fromDate, int toDate, TrendCalculator trendCalculator) throws Exception {
    String fromDatestr = String.valueOf(fromDate);
    int fromday = Integer.parseInt(fromDatestr.substring(6));
    int frommon = Integer.parseInt(fromDatestr.substring(4, 6));
    int fromyear = Integer.parseInt(fromDatestr.substring(0, 4));
    String toDatestr = String.valueOf(toDate);
    int today = Integer.parseInt(toDatestr.substring(6));
    int tomon = Integer.parseInt(toDatestr.substring(4, 6));
    int toyear = Integer.parseInt(toDatestr.substring(0, 4));
    return trend(fromday, frommon, fromyear, today, tomon, toyear, trendCalculator);
  }

  @Override
  public boolean buyOpportunity(int date, int mon, int year) throws Exception {
    Map<Integer, Double> map = getHistoricalClosing(date, mon, year,
            date, mon, year);

    //If the map size is not equal to one, which means the passed in argument is not a business day
    //throw exception.
    if (map.size() != 1) {
      throw new IllegalArgumentException("invalid input");
    }
    double ave50 = Calculate.getAve(this, 50, date, mon, year);
    double ave200 = Calculate.getAve(this, 200, date, mon, year);

    return ave50 > ave200;
  }

  /**
   * Get the stock symbol of this stock.
   *
   * @return the stock symbol of this stock
   */

  public String getStockSymbol() {
    return stockSymbol;
  }


  /**
   * Check whether the input is a valid stock symbol.
   * throw illegalArgumentException if the cannot find a stock with given stock symbol
   *
   * @throws Exception when the given stock symbol is in valid
   */
  public void isValid() throws Exception {
    String stockname = dataRetriever.getName(stockSymbol);
    if (stockname.equals("N/A")) {
      throw new IllegalArgumentException("invalid stock symbol");
    }
  }

  /**
   * Override the equals function.
   * Return true if given object is a Stock, and has same stock symbol.
   *
   * @param obj other object
   * @return true if given object is a Stock, and has same stock symbol, otherwise false.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof IStock) {
      Stock stock = (Stock) obj;
      return this.getStockSymbol().equals(stock.getStockSymbol());
    } else {
      return false;
    }
  }

  /**
   * Override the hashCode function.
   *
   * @return the hashcode of stock symbol
   */
  @Override
  public int hashCode() {
    return stockSymbol.hashCode();
  }

  @Override
  public String toString() {
    try {
      return stockSymbol + dataRetriever.getName(stockSymbol);
    } catch (Exception e) {
      return "invalid stock";
    }
  }

  @Override
  public Map<Integer, Double> getAveRange(int fromDate, int toDate, int days)
          throws Exception {
    return Calculate.getAveRange(this, fromDate, toDate, days);
  }


}

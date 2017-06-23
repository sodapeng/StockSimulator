package model.trader;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import util.Calculate;
import util.DateUtil;
import util.PriceRecord;
import util.StockDataRetriever;


/**
 * This is a class represent a basket of stocks.
 */
public class Basket implements IStock {

  private Map<Stock, Integer> basketMap;
  private StockDataRetriever dataRetriever;
  private String basektname;
  private int basektCreateTime;

  /**
   * Construct a basket object.
   * Create a basket with a basket name.
   *
   * @param basketname    the name of this basket
   * @param dataRetriever a stock data retriever
   * @param createDate    the create date of this bakset
   */
  public Basket(String basketname, StockDataRetriever dataRetriever, int createDate) {
    this.basektname = basketname;
    this.dataRetriever = dataRetriever;
    this.basketMap = new HashMap<>();
    this.basektCreateTime = createDate;
  }


  /**
   * Calculate the price of this basket on a certain date.
   * Return the closing price of this basket of stock on a certain date.
   *
   * @param date the day of a certain date
   * @param mon  the month of a certain date
   * @param year the year of a certain date
   * @return the closing price of this basket of stock on a certain date
   * @throws Exception when cannot retrieve data
   */
  //throw exception if given date is before basket create date
  public double getBasketPrice(int date, int mon, int year) throws Exception {
    if (beforeCreate(date, mon, year)) {
      throw new IllegalArgumentException("invalid input");
    }
    return getPrice(date, mon, year).getClosePrice();
  }

  @Override
  //throw exception if given date is before basket create date
  public PriceRecord getPrice(int date, int mon, int year) throws Exception {
    if (beforeCreate(date, mon, year)) {
      throw new IllegalArgumentException("invalid input");
    }
    double openprice = 0;
    double closeprice = 0;
    double highestprice = 0;
    double lowestprice = 0;

    for (Map.Entry<Stock, Integer> e : basketMap.entrySet()) {
      PriceRecord record = Calculate.getPrice(e.getKey().getStockSymbol(), dataRetriever,
              date, mon, year);
      openprice += record.getOpenPrice() * e.getValue();
      closeprice += record.getClosePrice() * e.getValue();
      highestprice += record.getHighestDayPrice() * e.getValue();
      lowestprice += record.getLowestDayPrice() * e.getValue();
    }
    return new PriceRecord(openprice, closeprice, lowestprice, highestprice);
  }

  @Override
  //throw exception if from date is before basket create date
  public Map<Integer, Double> getHistoricalClosing(int fromDate, int fromMon, int fromYear,
                                                   int toDate, int toMon, int toYear) throws Exception {
    if (beforeCreate(fromDate, fromMon, fromYear)) {
      throw new IllegalArgumentException("invalid input");
    }
    Map<Integer, Double> basketHC = new TreeMap<>();
    for (Map.Entry<Stock, Integer> e : basketMap.entrySet()) {
      Map<Integer, Double> map = e.getKey().getHistoricalClosing(fromDate, fromMon, fromYear,
              toDate, toMon, toYear);
      for (int time : map.keySet()) {
        if (!basketHC.containsKey(time)) {
          basketHC.put(time, map.get(time) * e.getValue());
        } else {
          basketHC.put(time, basketHC.get(time) + map.get(time) * e.getValue());
        }
      }
    }
    return basketHC;

  }

  @Override
  //throw exception if from date is before basket create date
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
  //throw exception if from date is before basket create date
  public String trend(int fromDate, int fromMon, int fromYear,
                      int toDate, int toMon, int toYear, TrendCalculator trendCalculator)
          throws Exception {
    if (beforeCreate(fromDate, fromMon, fromYear)) {
      throw new IllegalArgumentException("invalid input");
    }
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
  //throw exception if from date is before basket create date
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
  //throw exception if from date is before basket create date
  public boolean buyOpportunity(int date, int mon, int year) throws Exception {
    if (beforeCreate(date, mon, year)) {
      throw new IllegalArgumentException("invalid input");
    }

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

  @Override
  //throw exception if from date is before basket create date
  //throw exception if moving average data cannot be calculated. 50 days and 200 days can only
  //be calculated if 50 days and 200 days before given from date is later than basket creation
  //date, or it will throw exception.

  public Map<Integer, Double> getAveRange(int fromDate, int toDate, int days) throws Exception {
    return Calculate.getAveRange(this, fromDate, toDate, days);
  }

  /**
   * Generate a map with stock symbol as key, and stock share as value.
   *
   * @return the map view of a basket
   */
  public Map<String, Integer> getStockMap() {
    Map<String, Integer> map = new HashMap<>();
    for (Map.Entry<Stock, Integer> e : basketMap.entrySet()) {
      map.put(e.getKey().getStockSymbol(), e.getValue());
    }
    return map;
  }

  /**
   * Get a new copy of map which contains the stocks and share in this basket.
   *
   * @return the map with stocks in this basket as key and share as value
   */
  public Map<Stock, Integer> getBasket() {
    return new HashMap<Stock, Integer>(basketMap);
  }

  /**
   * Add shares of stocks to an existing basket using its ticker symbol.
   *
   * @param stockSymbol the stock symbol need to be added
   * @param share       the share need to be added
   * @throws Exception cannot retrieve data
   */
  public void addStock(String stockSymbol, int share) throws Exception {
    Stock newstock = new Stock(stockSymbol, dataRetriever);
    if (basketMap.containsKey(newstock)) {
      basketMap.put(newstock, basketMap.get(newstock) + share);
    } else {
      basketMap.put(newstock, share);
    }
  }

  @Override
  public String toString() {
    StringBuilder basketstr = new StringBuilder();
    for (Map.Entry<Stock, Integer> e : basketMap.entrySet()) {
      basketstr.append("stock symbol: ");
      basketstr.append(e.getKey().getStockSymbol());
      basketstr.append("stock share: ");
      basketstr.append(e.getValue());
    }
    return basketstr.toString();
  }

  /**
   * Return the basket name.
   *
   * @return name of this basket
   */
  public String getBasektname() {
    return this.basektname;
  }

  /**
   * Return the basket creation date.
   *
   * @return date basket created
   */
  public int getBasektCreateTime() {
    return this.basektCreateTime;
  }

  /**
   * Check if given date is before basket creation date.
   *
   * @param date date of given date
   * @param mon  mon of given date
   * @param year year of given date
   * @return true if given date is before basket creation date, otherwise false
   */
  private boolean beforeCreate(int date, int mon, int year) {
    LocalDate fromlocal = DateUtil.processDayMonYear(date, mon, year);
    int fromint = DateUtil.convertInt(fromlocal);
    return fromint < basektCreateTime;
  }
}

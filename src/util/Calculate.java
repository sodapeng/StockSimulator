package util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.trader.IStock;


/**
 * This is utility class to support the calculation of related to stock and basket.
 */
public class Calculate {
  /**
   * Get the price of a stock or a basket of stocks on a certain day with given stock number and
   * stock data retriever and date.
   * Return a PriceRecord object which contains the highest/lowest/open/closing price of a stock or
   * a basket of stocks on a certain day.
   *
   * @param date the day of a certain day
   * @param mon  the month of a certain day
   * @param year the year of a certain day
   * @return a PriceRecord of a stock or a basket of stocks on a certain day
   * @throws Exception when cannot retrieve data
   */
  public static PriceRecord getPrice(String stockSymbol, StockDataRetriever dataRetriever,
                                     int date, int mon, int year) throws Exception {

    Map<Integer, PriceRecord> map = dataRetriever.getHistoricalPrices(stockSymbol, date, mon, year,
            date, mon, year);

    //If the map size is not equal to one, which means the passed in argument is not a business day
    //throw exception.
    if (map.size() != 1) {
      throw new IllegalArgumentException("invalid input");
    }
    List<Integer> keylist = new ArrayList<>(map.keySet());
    int key = keylist.get(0);
    return map.get(key);
  }

  /**
   * Calculate the The X-day moving average on day Y of a stock or a basket of stocks
   * Calculate average of the closing price of a tradable item for the last X business days,
   * starting on day Y.
   *
   * @param stock a IStock
   * @param days  the last X business days
   * @param date  the day of a certain date
   * @param mon   the month of a certain date
   * @param year  the year of a certain date
   * @throws Exception when cannot retrieve data
   * @retrun the X-day moving average
   */
  public static double getAve(IStock stock, int days, int date, int mon, int year)
          throws Exception {
    int minusDays = days * 2;
    LocalDate startDay = DateUtil.minusDay(minusDays, date, mon, year);
    int startdate = startDay.getDayOfMonth();
    int startmon = startDay.getMonthValue();
    int startyear = startDay.getYear();
    double sumclosing = 0;
    int count = days;

    //Retrieve more historical closing data to get needed business days.
    Map<Integer, Double> mapHC = stock.getHistoricalClosing(startdate, startmon, startyear,
            date, mon, year);
    List<Integer> sortedkeys = new ArrayList<Integer>(mapHC.keySet());

    //Sorted the date integer/key.
    Collections.sort(sortedkeys);
    for (int i = sortedkeys.size() - 1; i >= 0; i--) {
      if (count != 0) {
        sumclosing += mapHC.get(sortedkeys.get(i));
        count--;
      } else {
        break;
      }
    }
    return sumclosing / days;
  }

  /**
   * Calculate the The X-day moving average on day Y of a stock or a basket of stocks within a
   * certain date range.
   *
   * @param iStock   a IStock
   * @param fromDate start of date range
   * @param toDate   end of date range
   * @param days     last X business days
   * @return X-day moving average within certain date range
   * @throws Exception when cannot retrieve data
   */
  public static Map<Integer, Double> getAveRange(IStock iStock, int fromDate, int toDate, int days)
          throws Exception {
    LocalDate fromlocal = DateUtil.getLocalDate(fromDate);

    Map<Integer, Double> mapHCrange = iStock.getHistoricalClosing(fromDate, toDate);

    //calculate the 2 times more days before from date as startdays
    LocalDate startDay = DateUtil.minusDay(days * 2, fromlocal);

    int startint = DateUtil.convertInt(startDay);
    double sumclosing = 0;
    int count = days;

    //use queue to store data got previously, and keep polling and offering old and new data
    LinkedList<Double> queue = new LinkedList<>();

    //Retrieve historical pricing data from start date to from date.
    Map<Integer, Double> mapHC = iStock.getHistoricalClosing(startint, fromDate);

    List<Integer> sortedkeys = new ArrayList<Integer>(mapHC.keySet());

    //Sorted the date integer/key.
    Collections.sort(sortedkeys);

    for (int i = sortedkeys.size() - 1; i >= 0; i--) {
      if (count != 0) {
        queue.offerLast(mapHC.get(sortedkeys.get(i)));
        sumclosing += mapHC.get(sortedkeys.get(i));
        count--;
      } else {
        break;
      }
    }

    Map<Integer, Double> ave50 = new HashMap<>();
    int curint = sortedkeys.get(sortedkeys.size() - 1);

    LocalDate curlocal = DateUtil.getLocalDate(curint);

    ave50.put(curint, sumclosing / days);

    curlocal = curlocal.plusDays(1);
    curint = DateUtil.convertInt(curlocal);
    double pre = 0;
    while (curint <= toDate) {
      if (mapHCrange.containsKey(curint)) {
        pre = queue.pollLast();
        sumclosing = sumclosing - pre + mapHCrange.get(curint);
        ave50.put(curint, sumclosing / days);

        queue.offerFirst(mapHCrange.get(curint));
      }
      curlocal = curlocal.plusDays(1);
      curint = DateUtil.convertInt(curlocal);

    }

    return ave50;
  }
}

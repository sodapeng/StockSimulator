package model.trader;

import java.util.HashMap;
import java.util.Map;

import util.StockDataRetriever;

/**
 * This is a class represent a IStock Model
 * It stores baskets and stocks information received from user input.
 */
public class IStockModel implements Model {
  private Map<String, Basket> setOfBasket;

  private StockDataRetriever dataRetriever;

  /**
   * Create an empty IStock set.
   *
   * @param stockDataRetriever stockdataretriever
   */
  public IStockModel(StockDataRetriever stockDataRetriever) {
    this.setOfBasket = new HashMap<>();
    this.dataRetriever = stockDataRetriever;

  }

  @Override
  public Map<String, Map<Integer, Double>> iStockPlot(String iStockName, int fromDate, int toDate)
          throws Exception {
    Map<String, Map<Integer, Double>> istockplot = new HashMap<>();
    //Check if given IStock name is a basket.
    if (setOfBasket.containsKey(iStockName)) {
      Map<Integer, Double> data
              = setOfBasket.get(iStockName).getHistoricalClosing(fromDate, toDate);
      istockplot.put(iStockName, data);
      return istockplot;
    } else {
      //Check if given IStock name is a valid stock name.
      if (dataRetriever.getName(iStockName).equals("N/A")) {
        throw new IllegalArgumentException("Invalid stock/basket name");
      } else {
        Map<Integer, Double> data
                = new Stock(iStockName, dataRetriever).getHistoricalClosing(fromDate, toDate);
        istockplot.put(iStockName, data);
        return istockplot;
      }
    }
  }

  @Override
  public Map<String, Map<Integer, Double>> iStockPlotMov(String iStockName, int fromDate,
                                                         int toDate, int days) throws Exception {

    Map<String, Map<Integer, Double>> move = new HashMap<>();
    IStock istock;
    //Check if given IStock name is a basket.
    if (setOfBasket.containsKey(iStockName)) {
      istock = setOfBasket.get(iStockName);
    } else {
      //Check if given IStock name is a valid stock name.
      if (dataRetriever.getName(iStockName).equals("N/A")) {
        throw new IllegalArgumentException("Invalid stock/basket name");
      } else {
        istock = new Stock(iStockName, dataRetriever);
      }
    }
    //Retrieve 50 and 200 moving average data.
    if (days == 50 || days == 250) {
      move.put(iStockName + 50, istock.getAveRange(fromDate, toDate, 50));
    }
    if (days == 200 || days == 250) {
      move.put(iStockName + 200, istock.getAveRange(fromDate, toDate, 200));
    }
    return move;
  }


  @Override
  public void add(String basketName, StockDataRetriever dataRetriever, int createDate) {
    Basket basket = new Basket(basketName, dataRetriever, createDate);
    setOfBasket.put(basketName, basket);
  }

  /**
   * Return a new copy of this basket set.
   *
   * @return this basket set
   */
  public Map<String, Basket> getBasketSet() {
    return new HashMap<String, Basket>(setOfBasket);
  }


  @Override
  public void addStock(String basketName, String stockSymbol, int share) throws Exception {
    Basket request = setOfBasket.get(basketName);
    request.addStock(stockSymbol, share);
    setOfBasket.put(basketName, request);
  }


  @Override
  public String trend(String iStockName, int fromdate, int todate, TrendCalculator trendCalculator)
          throws Exception {
    IStock istock;
    if (setOfBasket.containsKey(iStockName)) {
      istock = setOfBasket.get(iStockName);
      return istock.trend(fromdate, todate, trendCalculator);
    } else {
      if (dataRetriever.getName(iStockName).equals("N/A")) {
        throw new IllegalArgumentException("Invalid stock/basket name");
      } else {
        istock = new Stock(iStockName, dataRetriever);
        return istock.trend(fromdate, todate, trendCalculator);
      }
    }
  }

  /**
   * Check if given basket name has been created or not.
   *
   * @param basketName given basket name.
   * @return true if basket has already been created, otherwise false
   */
  public boolean containsBasket(String basketName) {
    return setOfBasket.containsKey(basketName);
  }
}

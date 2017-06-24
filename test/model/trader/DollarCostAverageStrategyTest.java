package model.trader;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import util.DateUtil;
import util.StockDataRetriever;
import util.WebStockDataRetriever;

import static org.junit.Assert.assertEquals;


public class DollarCostAverageStrategyTest {
  private Map<String, Double> proportionMap;
  private Map<String, Map<Integer, Double>> stockPriceRecord;
  private int startDate = 20170502;
  private LocalDate startlocal = DateUtil.getLocalDate(startDate);
  private int endDate = 20170609;
  private LocalDate endlocal = DateUtil.getLocalDate(endDate);
  private StockDataRetriever dataRetriever;
  private DollarCostAverageStrategy strategy;
  private Basket prebasket;
  private int investAmount;

  @Before
  public void setUp() throws Exception {
    dataRetriever = new WebStockDataRetriever();
    proportionMap = new HashMap<>();
    stockPriceRecord = new HashMap<>();
    prebasket = new Basket("test", dataRetriever, startDate);
    investAmount = 5000;
    proportionMap.put("AAPL", 0.5);
    proportionMap.put("AMZN", 0.5);
    for (Map.Entry<String, Double> e : proportionMap.entrySet()) {
      Map<Integer, Double> stockClosing =
              new Stock(e.getKey(), dataRetriever).getHistoricalClosing(20170502, 20170609);
      prebasket.addStock(e.getKey(), 0);
      stockPriceRecord.put(e.getKey(), stockClosing);
    }
    strategy = new DollarCostAverageStrategy(proportionMap, stockPriceRecord, dataRetriever);
  }


  /**
   * Test dollar cost average strategy return a correct basket with correct shares.
   */
  @Test
  public void invest() throws Exception {
    Basket newBasket = strategy.invest(prebasket, investAmount, startlocal);
    Map<String, Integer> invested = newBasket.getStockMap();
    assertEquals(new Integer(16), invested.get("AAPL"));
    assertEquals(new Integer(2), invested.get("AMZN"));

  }

  /**
   * Test dollar cost average strategy return a correct basket with correct shares when user has
   * a non-business day as investment day.
   */
  @Test
  public void investTest() throws Exception {
    startlocal = startlocal.minusDays(2);
    Basket newBasket = strategy.invest(prebasket, investAmount, startlocal);
    Map<String, Integer> invested = newBasket.getStockMap();
    assertEquals(new Integer(16), invested.get("AAPL"));
    assertEquals(new Integer(2), invested.get("AMZN"));

  }

  @Test
  public void getInvestCostTest() throws Exception {
    strategy.invest(prebasket, investAmount, startlocal);
    assertEquals(4254.04, strategy.getInvestingCost(), 0.0001);
  }
}
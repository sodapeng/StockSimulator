package util;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import model.trader.SimpleTrendCalculator;
import model.trader.Stock;
import model.trader.TrendCalculator;

import static org.junit.Assert.assertEquals;

/**
 * This is JUnit test for simple trend calculator class.
 */
public class SimpleTrendCalculatorTest {
  private double epsilon = 0.0001;
  private Stock stock1;
  private Stock stock2;
  private Stock stock3;
  private StockDataRetriever dataRetriever = new WebStockDataRetriever();
  private TrendCalculator calculator = new SimpleTrendCalculator();

  /**
   * Set up for simple trend calculator test.
   */
  @Before
  public void setUp() throws Exception {
    stock1 = new Stock("GOOG", dataRetriever);
    stock2 = new Stock("AAPL", dataRetriever);
    stock3 = new Stock("JNUG", dataRetriever);
  }

  /**
   * Tests if trend/price change is correctly calculated for the stock for the given dates.
   */
  @Test
  public void trendTest() throws Exception {
    Map<Integer, Double> map1down = stock1.getHistoricalClosing(
            6, 9, 2016, 9, 9, 2016);
    Map<Integer, Double> map1up = stock1.getHistoricalClosing(
            17, 10, 2016, 19, 10, 2016);
    double trend1 = calculator.trend(map1down);
    double trend2 = calculator.trend(map1up);
    assertEquals(-20.42, trend1, epsilon);
    assertEquals(21.60, trend2, epsilon);
  }

  /**
   * Tests if the method returns the correct trend description for each possible trend scenario.
   */
  @Test
  public void trendStatusTest() throws Exception {
    Map<Integer, Double> map1 = stock2.getHistoricalClosing(6, 6, 2017,
            6, 13, 2017);
    Map<Integer, Double> map2 = stock2.getHistoricalClosing(
            14, 6, 2016, 13, 6, 2017);
    Map<Integer, Double> map3 = stock2.getHistoricalClosing(
            13, 3, 2017, 5, 6, 2017);
    Map<Integer, Double> map4 = stock3.getHistoricalClosing(
            8, 7, 2016, 13, 6, 2017);
    assertEquals("Gentle decline", calculator.getTrendStatus(calculator.trend(map1)));
    assertEquals("Strong incline", calculator.getTrendStatus(calculator.trend(map2)));
    assertEquals("Gentle incline", calculator.getTrendStatus(calculator.trend(map3)));
    assertEquals("Strong decline", calculator.getTrendStatus(calculator.trend(map4)));
  }
}
package model.trader;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import util.PriceRecord;
import util.StockDataRetriever;
import util.WebStockDataRetriever;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This is a JUnit test for stock class.
 */
public class StockTest {
  private double epsilon = 0.0001;
  private StockDataRetriever dataRetriever = new WebStockDataRetriever();
  private TrendCalculator calculator = new SimpleTrendCalculator();
  private Stock stock1;
  private Stock stock2;

  /**
   * Set up for stock test.
   */
  @Before
  public void setUp() throws Exception {
    stock1 = new Stock("AMZN", dataRetriever);
    stock2 = new Stock("AAPL", dataRetriever);
  }

  /**
   * Tests if the getStockSymbol method correctly returns the stock's symbol.
   */
  @Test
  public void getSymbolTest() {
    assertEquals("AMZN", stock1.getStockSymbol());
    assertEquals("AAPL", stock2.getStockSymbol());
  }

  /**
   * Tests if the method returns the correct trend description based on the given stock and dates.
   */
  @Test
  public void upTrendTest() throws Exception {
    assertEquals("Strong incline", stock2.trend(21, 7, 2016,
            6, 6, 2017, calculator));
    assertEquals("Gentle decline", stock2.trend(7, 6, 2017,
            13, 6, 2017, calculator));
    assertEquals("Strong decline", stock1.trend(5, 6, 2017,
            12, 6, 2017, calculator));
    assertEquals("Gentle incline", stock1.trend(1, 6, 2017,
            8, 6, 2017, calculator));


  }

  /**
   * Tests if the method correctly creates a map with date and closing price based on the given
   * stock and date.
   */
  @Test
  public void historicalClosingTest() throws Exception {
    Map<Integer, Double> result1 = stock1.getHistoricalClosing(5, 6, 2017,
            9, 6, 2017);
    Map<Integer, Double> result2 = stock2.getHistoricalClosing(26, 5, 2017,
            31, 5, 2017);
    assertEquals(1011.34, result1.get(20170605), epsilon);
    assertEquals(1003, result1.get(20170606), epsilon);
    assertEquals(1010.07, result1.get(20170607), epsilon);
    assertEquals(1010.27, result1.get(20170608), epsilon);
    assertEquals(978.31, result1.get(20170609), epsilon);
    assertEquals(153.61, result2.get(20170526), epsilon);
    assertEquals(153.67, result2.get(20170530), epsilon);
    assertEquals(152.76, result2.get(20170531), epsilon);
  }

  /**
   * Tests if the method correctly creates a map with date and closing price based on the given
   * stock and date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest() throws Exception {
    stock1.getPrice(27, 5, 2017);
  }

  /**
   * Tests if the getPrice method throws an exception when given a future date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest2() throws Exception {
    stock2.getPrice(27, 5, 2020);
  }

  /**
   * Tests if the stock constructor throws an exception when given an invalid symbol.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest3() throws Exception {
    Stock stock3 = new Stock("GOOG2", dataRetriever);
  }

  /**
   * Tests if an exception is thrown for a date range with a start date after the end date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest4() throws Exception {
    stock2.getHistoricalClosing(12, 6, 2017, 8, 6, 2017);
  }

  /**
   * Tests if an exception is thrown for a date range with the same start and end date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest5() throws Exception {
    stock2.trend(11, 6, 2017, 11, 6, 2017, calculator);
  }

  /**
   * Tests if an exception is thrown when an invalid (non-business) date is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest6() throws Exception {
    stock1.buyOpportunity(4, 6, 2017);
  }

  /**
   * Tests if the getPrice method creates a PriceRecord object with correct values for the
   * different price types.
   */
  @Test
  public void getPriceTest() throws Exception {
    PriceRecord result1 = stock1.getPrice(7, 6, 2017);
    PriceRecord result2 = stock2.getPrice(3, 5, 2017);
    assertEquals(1010.07, result1.getClosePrice(), epsilon);
    assertEquals(1010.25, result1.getHighestDayPrice(), epsilon);
    assertEquals(1002, result1.getLowestDayPrice(), epsilon);
    assertEquals(1005.95, result1.getOpenPrice(), epsilon);
    assertEquals(147.06, result2.getClosePrice(), epsilon);
    assertEquals(147.49, result2.getHighestDayPrice(), epsilon);
    assertEquals(144.27, result2.getLowestDayPrice(), epsilon);
    assertEquals(145.59, result2.getOpenPrice(), epsilon);

  }

  /**
   * Tests if the method correctly determines if there was a buy opportunity on the given date.
   */
  @Test
  public void buyOppTest() throws Exception {
    Stock stock3 = new Stock("BBRY", dataRetriever);
    assertFalse(stock3.buyOpportunity(21, 7, 2016));
    assertTrue(stock1.buyOpportunity(9, 6, 2017));
    assertTrue(stock2.buyOpportunity(9, 6, 2017));
  }

  @Test
  public void equalsTest() throws Exception {
    Stock stock3 = new Stock("GOOG", dataRetriever);
    Stock stock4 = new Stock("GOOG", dataRetriever);
    Stock stock5 = new Stock("APPL", dataRetriever);
    assertTrue(stock3.equals(stock4));
    assertTrue(stock4.equals(stock3));
    assertFalse(stock5.equals(stock3));
  }

}
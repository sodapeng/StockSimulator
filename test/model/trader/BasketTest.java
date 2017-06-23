package model.trader;

import org.junit.Before;
import org.junit.Test;


import java.util.Map;

import util.PriceRecord;
import util.StockDataRetriever;
import util.WebStockDataRetriever;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * This is a JUnit test for basket class.
 */

public class BasketTest {
  private double epsilon = 0.0001;
  private StockDataRetriever dataRetriever = new WebStockDataRetriever();
  private Basket basket1;
  private Basket basket2;
  private TrendCalculator calculator = new SimpleTrendCalculator();

  /**
   * Set up for basket test.
   */
  @Before
  public void setUp() throws Exception {
    basket1 = new Basket("basket1", dataRetriever, 20160101);
    basket2 = new Basket("basket2", dataRetriever, 20160101);
    basket1.addStock("MSFT", 10);
    basket1.addStock("GOOG", 5);
    basket2.addStock("MSFT", 10);
    basket2.addStock("GOOG", 5);
    basket2.addStock("AAPL", 2);
  }

  /**
   * Tests if the basket constructor correctly creates a list of the given stocks.
   */
  @Test
  public void testConstructor() throws Exception {
    Map<String, Integer> test = basket1.getStockMap();
    Map<Stock, Integer> test2 = basket2.getBasket();
    Stock st1 = new Stock("MSFT", dataRetriever);
    Stock st2 = new Stock("GOOG", dataRetriever);
    Stock st3 = new Stock("AAPL", dataRetriever);
    assertEquals(new Integer(10), test.get("MSFT"));
    assertEquals(new Integer(5), test.get("GOOG"));
    assertEquals(new Integer(2), test2.get(st3));
    assertEquals(new Integer(10), test2.get(st1));
    assertEquals(new Integer(5), test2.get(st2));

  }

  /**
   * Tests if the getPrice method throws an exception when an invalid date is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest() throws Exception {
    basket1.getPrice(2, 4, 2017);
  }


  /**
   * Tests if the getBasketPrice method throws an exception when an invalid date is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest1() throws Exception {
    basket1.getBasketPrice(4, 6, 2017);
  }

  /**
   * Tests if an exception is thrown for a date range with the same start and end date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest2() throws Exception {
    basket1.trend(5, 6, 2017, 5, 6, 2017, calculator);
  }

  /**
   * Tests if an exception is thrown for a date range with a start date after the end date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest5() throws Exception {
    basket1.trend(6, 6, 2017, 5, 6, 2017, calculator);
  }

  /**
   * Tests if the buyOpportunity method throws an exception when an invalid date is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest3() throws Exception {
    basket1.buyOpportunity(4, 6, 2017);
  }

  /**
   * Tests if the getHistoricalClosing method throws an exception for an invalid date range.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest4() throws Exception {
    basket1.getHistoricalClosing(4, 6, 2017, 4, 6, 2017);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest6() throws Exception {
    basket1.getHistoricalClosing(1, 1, 2015, 1, 1, 2017);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest7() throws Exception {
    basket1.buyOpportunity(1, 1, 2015);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest8() throws Exception {
    basket1.trend(1, 1, 2015, 1, 1, 2017, calculator);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest9() throws Exception {
    basket1.getHistoricalClosing(20150101, 20170101);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest10() throws Exception {
    basket1.buyOpportunity(1, 1, 2015);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest11() throws Exception {
    basket1.trend(20150101, 20170101, calculator);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest12() throws Exception {
    basket1.getAveRange(20150101, 20170101, 50);
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest13() throws Exception {
    basket1.getAveRange(20150101, 20170101, 200);
  }


  /**
   * Tests if the method correctly calculates the basket price for a given date.
   */
  @Test
  public void getBasketPriceTest() throws Exception {
    assertEquals(4830.15, basket1.getBasketPrice(4, 4, 2017), epsilon);
    assertEquals(5750.31, basket2.getBasketPrice(9, 6, 2017), epsilon);
  }

  /**
   * Tests if the getPrice method creates a PriceRecord object for the given basket with correct
   * values for the different price types.
   */
  @Test
  public void getPriceTest() throws Exception {
    PriceRecord result1 = basket1.getPrice(4, 4, 2017);
    PriceRecord result2 = basket2.getPrice(19, 4, 2017);
    PriceRecord result3 = basket1.getPrice(9, 6, 2017);
    PriceRecord result4 = basket2.getPrice(9, 6, 2017);
    assertEquals(4830.15, result1.getClosePrice(), epsilon);
    assertEquals(4834, result1.getHighestDayPrice(), epsilon);
    assertEquals(4798, result1.getLowestDayPrice(), epsilon);
    assertEquals(4810.7, result1.getOpenPrice(), epsilon);
    assertEquals(5122.81, result2.getClosePrice(), epsilon);
    assertEquals(5152.6, result2.getHighestDayPrice(), epsilon);
    assertEquals(5111.25, result2.getLowestDayPrice(), epsilon);
    assertEquals(5139.21, result2.getOpenPrice(), epsilon);
    assertEquals(5452.35, result3.getClosePrice(), epsilon);
    assertEquals(5750.31, result4.getClosePrice(), epsilon);
  }

  /**
   * Tests if the method correctly creates a map with date and closing price based on the given
   * basket and date.
   */
  @Test
  public void historicalClosingTest() throws Exception {
    Map<Integer, Double> result1 = basket1.getHistoricalClosing(14, 3, 2017,
            17, 3, 2017);
    Map<Integer, Double> result2 = basket2.getHistoricalClosing(28, 3, 2017,
            31, 3, 2017);
    assertEquals(4872.2, result1.get(20170314), epsilon);
    assertEquals(4883.5, result1.get(20170315), epsilon);
    assertEquals(4890.3, result1.get(20170316), epsilon);
    assertEquals(4909.3, result1.get(20170317), epsilon);
    assertEquals(5045.1, result2.get(20170328), epsilon);
    assertEquals(5099.99, result2.get(20170329), epsilon);
    assertEquals(5102.46, result2.get(20170330), epsilon);
    assertEquals(5093.72, result2.get(20170331), epsilon);
  }

  /**
   * Tests if the method correctly determines if there was a buy opportunity on the given date.
   */
  @Test
  public void buyOppTest() throws Exception {
    Stock stock3 = new Stock("BBRY", dataRetriever);
    Basket basket3 = new Basket("basket3", dataRetriever, 20160101);
    basket3.addStock("BBRY", 10);
    assertTrue(basket1.buyOpportunity(9, 6, 2017));
    assertTrue(basket2.buyOpportunity(9, 6, 2017));
  }

  /**
   * Tests if the method returns the correct trend description based on the given basket and dates.
   */
  @Test
  public void trendTest() throws Exception {
    assertEquals("Strong decline", basket1.trend(8, 6, 2017,
            13, 6, 2017, calculator));
    assertEquals("Gentle incline", basket1.trend(9, 6, 2017,
            13, 6, 2017, calculator));
    assertEquals("Strong incline", basket2.trend(9, 6, 2016,
            13, 6, 2017, calculator));
  }


}
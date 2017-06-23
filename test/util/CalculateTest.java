package util;

import org.junit.Before;
import org.junit.Test;

import model.trader.Basket;
import model.trader.Stock;

import static org.junit.Assert.assertEquals;

/**
 * This is a JUnit test for calculate class.
 */
public class CalculateTest {
  private double epsilon = 0.0001;
  private StockDataRetriever dataRetriever = new WebStockDataRetriever();
  private Stock stock1;
  private Stock stock2;
  private Basket basket1;

  /**
   * Set up for calculate test.
   */
  @Before
  public void setUp() throws Exception {
    stock1 = new Stock("GOOG", dataRetriever);
    stock2 = new Stock("MSFT", dataRetriever);
    basket1 = new Basket("basket1", dataRetriever, 20150101);
    basket1.addStock("GOOG", 10);
    basket1.addStock("MSFT", 20);
    basket1.addStock("AAPL", 30);
  }

  /**
   * Tests if the method correctly calculates the moving avg for given date and stock/basket.
   */
  @Test
  public void movingAveTest() throws Exception {
    double result150 = Calculate.getAve(stock1, 50, 23, 5, 2017);
    double result1200 = Calculate.getAve(stock1, 200, 23, 5, 2017);
    assertEquals(66.6992, Calculate.getAve(stock2, 50, 23, 5, 2017),
            epsilon);
    assertEquals(870.634, result150, epsilon);
    assertEquals(810.14775, result1200, epsilon);
    assertEquals(14101.514,
            Calculate.getAve(basket1, 50, 9, 5, 2017), epsilon);
  }

  /**
   * Tests if the getPrice method creates a PriceRecord object with correct values for the
   * different price types.
   */
  @Test
  public void getPriceTest() throws Exception {
    PriceRecord test1 = Calculate.getPrice(stock1.getStockSymbol(), dataRetriever,
            13, 6, 2017);
    assertEquals(951.91, test1.getOpenPrice(), epsilon);
    assertEquals(953.4, test1.getClosePrice(), epsilon);
    assertEquals(959.98, test1.getHighestDayPrice(), epsilon);
    assertEquals(944.09, test1.getLowestDayPrice(), epsilon);
  }

  /**
   * Tests if an exception is thrown when an invalid date is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest() throws Exception {
    stock1.getPrice(4, 6, 2017);
  }

  /**
   * Tests if an exception is thrown when an invalid time period is given.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgsTest2() throws Exception {
    double result = Calculate.getAve(stock1, 1, 1, 7, 2017);
  }
}

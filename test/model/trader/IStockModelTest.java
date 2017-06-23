package model.trader;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import util.StockDataRetriever;
import util.WebStockDataRetriever;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhuangmira on 6/19/17.
 */
public class IStockModelTest {
  private IStockModel set;
  private StockDataRetriever dataRetriever;
  private double epsilon = 0.0001;
  private TrendCalculator trendCalculator;

  /**
   * Set up for InteractiveView test.
   */
  @Before
  public void setUp() throws Exception {
    dataRetriever = new WebStockDataRetriever();
    set = new IStockModel(dataRetriever);
    set.add("b1", dataRetriever, 20150101);
    set.addStock("b1", "AAPL", 10);
    trendCalculator = new SimpleTrendCalculator();


  }

  @Test
  public void iStockPlot() throws Exception {
    Map<String, Map<Integer, Double>> res = set.iStockPlot("AAPL", 20170601,
            20170602);
    Map<Integer, Double> aapl = res.get("AAPL");
    assertEquals(153.18, aapl.get(20170601), epsilon);
    assertEquals(155.45, aapl.get(20170602), epsilon);
    Map<String, Map<Integer, Double>> res1 = set.iStockPlot("b1", 20170601,
            20170602);
    Map<Integer, Double> basket = res1.get("b1");
    assertEquals(1531.8, basket.get(20170601), epsilon);
    assertEquals(1554.5, basket.get(20170602), epsilon);
  }

  /**
   * Test invalid stock symbol.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgs1() throws Exception {
    Map<String, Map<Integer, Double>> res = set.iStockPlot("AAPL2", 20170601,
            20170602);
  }

  /**
   * Test invalid basket name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgs2() throws Exception {
    Map<String, Map<Integer, Double>> res = set.iStockPlot("b2", 20170601,
            20170602);
  }


  @Test
  public void iStockPlotMov() throws Exception {
    Map<String, Map<Integer, Double>> res = set.iStockPlotMov("AAPL", 20170601,
            20170602, 50);
    Map<Integer, Double> aapl = res.get("AAPL50");
    assertEquals(147.1282, aapl.get(20170601), epsilon);
    assertEquals(147.4088, aapl.get(20170602), epsilon);
    Map<String, Map<Integer, Double>> res1 = set.iStockPlotMov("b1", 20170601,
            20170602, 200);
    Map<Integer, Double> basket = res1.get("b1200");
    assertEquals(1257.5605, basket.get(20170601), epsilon);
    assertEquals(1259.864, basket.get(20170602), epsilon);
  }

  /**
   * Test moving average cannot be calculated if it 200 or 50 days moving average date is earlier
   * than basket creation date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void iStockPlotMov2() throws Exception {
    dataRetriever = new WebStockDataRetriever();
    set = new IStockModel(dataRetriever);
    set.add("b3", dataRetriever, 20170101);
    set.addStock("b3", "AAPL", 10);
    trendCalculator = new SimpleTrendCalculator();
    Map<String, Map<Integer, Double>> res1 = set.iStockPlotMov("b3",
            20170601, 20170602, 200);
  }

  /**
   * Test invalid stock name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgs3() throws Exception {
    Map<String, Map<Integer, Double>> res = set.iStockPlotMov("AAPL2",
            20170601, 20170602, 50);
  }

  /**
   * Test invalid basket name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgs4() throws Exception {
    Map<String, Map<Integer, Double>> res = set.iStockPlotMov("b2", 20170601,
            20170602, 200);
  }

  @Test
  public void add() throws Exception {
    set.add("b2", dataRetriever, 20170101);
    set.addStock("b2", "AMZN", 10);
    Map<String, Basket> basketMap = set.getBasketSet();
    assertEquals("stock symbol: AAPLstock share: 10", basketMap.get("b1").toString());
    assertEquals("stock symbol: AMZNstock share: 10", basketMap.get("b2").toString());
  }

  @Test
  public void getBasketSet() throws Exception {
    Map<String, Basket> basketMap = set.getBasketSet();
    assertEquals("stock symbol: AAPLstock share: 10", basketMap.get("b1").toString());
  }

  @Test
  public void addStock() throws Exception {
    set.addStock("b1", "AAPL", 20);
    set.addStock("b1", "AMZN", 10);
    Map<String, Basket> basketMap = set.getBasketSet();
    assertEquals("stock symbol: AAPLstock share: 30stock symbol: AMZNstock share: 10",
            basketMap.get("b1").toString());
  }

  @Test
  public void trend() throws Exception {
    String res = set.trend("AAPL", 20170601, 20170602, trendCalculator);
    assertEquals("Gentle incline", res);
    String res1 = set.trend("b1", 20170601, 20170602, trendCalculator);
    assertEquals("Gentle incline", res1);
  }


  @Test
  public void containsBasket() throws Exception {
    assertTrue(set.containsBasket("b1"));
    assertFalse(set.containsBasket("b2"));
  }

  /**
   * Test invalid stock name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgs5() throws Exception {
    String res = set.trend("AAPL2", 20170601, 20170602,
            trendCalculator);
  }

  /**
   * Test invalid basket name.
   */
  @Test(expected = IllegalArgumentException.class)
  public void illegalArgs6() throws Exception {
    String res = set.trend("b2", 20170601, 20170602, trendCalculator);
  }


}
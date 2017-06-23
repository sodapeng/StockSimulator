package model.trader;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import util.DateUtil;
import util.StockDataRetriever;

import static org.junit.Assert.*;

public class SimulatorTest {
  private int principle;
  private int investAmount;
  private LocalDate startDate;
  private LocalDate endDate;
  private String strategy;
  private String cadence;
  private Map<String, Double> proportionMap;
  private Simulator simulator;
  private StockDataRetriever dataRetriever;

  @Before
  public void setUp() throws Exception {
    proportionMap = new HashMap<>();
    proportionMap.put("AAPL", 0.5);
    proportionMap.put("AMZN", 0.5);
    startDate = DateUtil.getLocalDate(20170502);
    startDate = DateUtil.getLocalDate(20170602);
    principle = 5000;
    investAmount = 1000;
    strategy = "DCA";
    cadence = "MONTH";
    Simulator simulator = new Simulator(principle, investAmount, startDate, endDate, strategy,
            cadence, proportionMap, dataRetriever);
  }

  /**
   * Test when stock proportion added up not equal to 1.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testConstructor1() throws Exception{
    proportionMap = new HashMap<>();
    proportionMap.put("AAPL", 0.5);
    proportionMap.put("AMZN", 0.3);
    startDate = DateUtil.getLocalDate(20170502);
    startDate = DateUtil.getLocalDate(20170602);
    principle = 5000;
    investAmount = 1000;
    strategy = "DCA";
    cadence = "MONTH";
    Simulator simulator = new Simulator(principle, investAmount, startDate, endDate, strategy,
            cadence, proportionMap, dataRetriever);
  }

  /**
   * Test when strategy is not valid.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testConstructor2() throws Exception{
    proportionMap = new HashMap<>();
    proportionMap.put("AAPL", 0.5);
    proportionMap.put("AMZN", 0.3);
    startDate = DateUtil.getLocalDate(20170502);
    startDate = DateUtil.getLocalDate(20170602);
    principle = 5000;
    investAmount = 1000;
    strategy = "DCAB";
    cadence = "MONTH";
    Simulator simulator = new Simulator(principle, investAmount, startDate, endDate, strategy,
            cadence, proportionMap, dataRetriever);
  }

  /**
   * Test when cadence is not valid.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testConstructor3() throws Exception{
    proportionMap = new HashMap<>();
    proportionMap.put("AAPL", 0.5);
    proportionMap.put("AMZN", 0.3);
    startDate = DateUtil.getLocalDate(20170502);
    startDate = DateUtil.getLocalDate(20170602);
    principle = 5000;
    investAmount = 1000;
    strategy = "DCAB";
    cadence = "WEEK";
    Simulator simulator = new Simulator(principle, investAmount, startDate, endDate, strategy,
            cadence, proportionMap, dataRetriever);
  }

  @Test
  public void getProfit() throws Exception {
    assertEquals(0.0, simulator.getProfit(DateUtil.getLocalDate(20170601)), 0.0001);
    assertEquals(0.0, simulator.getProfit(DateUtil.getLocalDate(20170526)), 0.0001);
  }

  /**
   * Test get profit throw exception when date is not in investment range.
   */
  @Test (expected = IllegalArgumentException.class)
  public void getProfitTest() {
    simulator.getProfit(DateUtil.getLocalDate(20170603));
  }

  @Test (expected = IllegalArgumentException.class)
  public void getProfitTes1t() {
    simulator.getProfit(DateUtil.getLocalDate(20170403));
  }

}
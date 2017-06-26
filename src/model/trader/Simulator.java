package model.trader;

import util.DateUtil;
import util.PriceRecord;
import util.StockDataRetriever;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This is a simulator class represents a specific simulation user may apply.
 */

public class Simulator implements ISimulator {

  private TreeMap<Integer, Basket> basketSnapshots;
  private InvestingStrategy strategy;
  private LocalDate startDate;
  private LocalDate endDate;
  private Map<String, Double> proportionMap;
  private double principle;
  private double investAmount;
  private Map<String, Map<Integer, Double>> stockPricesRecord;
  private String cadence;
  private StockDataRetriever dataRetriever;

  /**
   * Support command "[-simulate -run principle investingAmount startDate endDate
   * DOLLARCOSTAVERAGE/OPTION2/OPTION3 MONTH/QUARTER {a list of stock proportion pairs}]".
   *
   * @param principle     principle
   * @param investAmount  the amount of money used in each investment
   * @param startDate     simulation start date
   * @param endDate       simulation end date
   * @param strategy      simulation strategy
   * @param cadence       investment cadence
   * @param proportionMap stock to proportion map, proprotions must add up to 1
   */
  public Simulator(double principle, double investAmount, LocalDate startDate, LocalDate endDate,
                   String strategy, String cadence, Map<String, Double> proportionMap,
                   StockDataRetriever dataRetriever) throws Exception {
    this.principle = principle;
    this.investAmount = investAmount;

    this.startDate = setStartDate(startDate);
    this.endDate = setEndDate(endDate);

    this.cadence = setCadance(cadence);

    this.proportionMap = setProportion(proportionMap);

    this.dataRetriever = dataRetriever;

    this.stockPricesRecord
            = fetchHistoricalStockPrices(this.proportionMap, this.startDate, this.endDate);

    this.strategy
            = setStrategy(strategy, this.stockPricesRecord, this.dataRetriever);

    Basket initialBasket
            = this.strategy.invest(initBasket(this.proportionMap), this.principle, this.startDate);
    this.principle = this.strategy.getInvestingCost();
    basketSnapshots = new TreeMap<Integer, Basket>();
    basketSnapshots.put(DateUtil.convertInt(this.startDate), initialBasket);


    // run simulation
    simulate();
  }

  /**
   * Get profit at a given time.
   * Profit is based on actual initial principle, and money that is invested periodically.
   * Share of each stocks in an basket is calculated by proportion * principle / stock price. Then
   * the share will be casted in to integer as final share of this stock in this basket. Then the
   * actual initial principle will be calculated by each stocks' prices * its share.
   *
   * @param date the date at which profit is computed.
   * @return the profit
   */
  @Override
  public double getProfit(LocalDate date) {
    if (date.isBefore(this.startDate) || date.isAfter(this.endDate)) {
      throw new IllegalArgumentException("Date must be start and end range\n");
    }
    return getBasketValue(date) - principle;
  }

  /**
   * Start the simulation.
   *
   * @throws Exception when cannot retrieve data
   */
  private void simulate() throws Exception {
    LocalDate current = startDate;
    Basket curBasket = basketSnapshots.get(DateUtil.convertInt(current));
    int leap;
    if (cadence.equals("MONTH")) {
      leap = 1;
    } else {
      leap = 4;
    }
    while (current.plusMonths(leap).isBefore(endDate)) {
      current = current.plusMonths(leap);
      Basket newBasket = strategy.invest(curBasket, investAmount, current);
      this.principle += strategy.getInvestingCost();
      basketSnapshots.put(DateUtil.convertInt(current), newBasket);
      curBasket = newBasket;
    }
  }

  /**
   * Check if start date is earlier than 19000101.
   *
   * @param startDate start date
   * @return start date
   */
  private LocalDate setStartDate(LocalDate startDate) {
    if (startDate.isBefore(LocalDate.of(1900, 1, 1))) {
      throw new IllegalArgumentException("Start date must be later than 01/01/1900\n");
    }
    return startDate;
  }

  /**
   * Check if end date is later than current date.
   *
   * @param endDate end date
   * @return end date
   */
  private LocalDate setEndDate(LocalDate endDate) {
    if (endDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("End date must be no later than today\n");
    }
    return endDate;
  }

  /**
   * Create a Strategy object based on user choice of strategy.
   *
   * @param strategy          strategy will be used
   * @param stockPricesRecord stock price
   * @param dataRetriever     stock data retriever
   * @return Strategy object
   */
  private InvestingStrategy setStrategy(
          String strategy, Map<String, Map<Integer, Double>> stockPricesRecord,
          StockDataRetriever dataRetriever) {
    if (strategy.equals("DCA")) {
      return new DollarCostAverageStrategy(proportionMap, stockPricesRecord, dataRetriever);
    } else if (strategy.equals("AR")) {
      //Different kinds of strategy are supported, for now use DCA (implemented) as default.
      return new DollarCostAverageStrategy(proportionMap, stockPricesRecord, dataRetriever);
    } else {
      throw new IllegalArgumentException("Unknown Investing Strategy\n");
    }
  }

  /**
   * Set time interval for periodically investment.
   *
   * @param cadence user cadence input
   * @return cadence
   */
  private String setCadance(String cadence) {
    if (!cadence.equals("MONTH") && !cadence.equals("QUARTER")) {
      throw new IllegalArgumentException("Unknown Cadence\n");
    }
    return cadence;
  }

  /**
   * Check if user set stock proportion correctly.
   *
   * @param proportionMap stock proportion map
   * @return stock proportion map
   */
  private Map<String, Double> setProportion(Map<String, Double> proportionMap) {
    double sum = 0;
    for (String stock : proportionMap.keySet()) {
      sum += proportionMap.get(stock);
    }
    if (Math.abs(sum - 1) < 0.00001) {
      return proportionMap;
    } else {
      throw new IllegalArgumentException("Stock proportion must add up to 1\n");
    }
  }

  /**
   * Create a new basket for later investment.
   *
   * @param proportionMap stock proportion map
   * @return empty basket with 0 shares for all stocks
   * @throws Exception when cannot retrieve data.
   */
  private Basket initBasket(Map<String, Double> proportionMap) throws Exception {
    Basket basket =
            new Basket("SimulationBasket", this.dataRetriever, 19000101);
    for (String stock : proportionMap.keySet()) {
      basket.addStock(stock, 0);
    }
    return basket;
  }

  /**
   * Get historical pricing data of each stocks in basket within certain date range.
   *
   * @param proportionMap stock proportion map
   * @param startDate     start date
   * @param endDate       end date
   * @return historical pricing data map with stock symbol as key, data as value
   * @throws Exception when cannot retrieve data
   */
  private Map<String, Map<Integer, Double>> fetchHistoricalStockPrices(
          Map<String, Double> proportionMap, LocalDate startDate, LocalDate endDate)
          throws Exception {
    // fetch two more week data because if we invest on weekends we will use prices in next monday
    endDate = endDate.plusWeeks(2);
    Map<String, Map<Integer, Double>> stockPricesRecord =
            new HashMap<String, Map<Integer, Double>>();
    for (String stock : proportionMap.keySet()) {
      Map<Integer, PriceRecord> dateToPricRecordeMap = dataRetriever.getHistoricalPrices(stock,
              startDate.getDayOfMonth(), startDate.getMonthValue(), startDate.getYear(),
              endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear());
      Map<Integer, Double> dateToClosingPriceMap = new HashMap<Integer, Double>();
      for (int date : dateToPricRecordeMap.keySet()) {
        dateToClosingPriceMap.put(date, dateToPricRecordeMap.get(date).getClosePrice());
      }
      stockPricesRecord.put(stock, dateToClosingPriceMap);
    }
    return stockPricesRecord;
  }

  /**
   * Get basket value on a given date.
   *
   * @param date date
   * @return value of basket
   */
  private double getBasketValue(LocalDate date) {
    double value = 0;
    Basket basket = basketSnapshots.floorEntry(DateUtil.convertInt(date)).getValue();
    for (Map.Entry<String, Integer> stock : basket.getStockMap().entrySet()) {
      int plusDays = 0;
      // assume that stock market never paused for more than two weeks,
      // or if we mistakenly look into the future
      // if the date looking for is not a business day, look for next monday
      while (stockPricesRecord.get(stock.getKey()).get(DateUtil.convertInt(date)) == null) {
        if (plusDays > 14) {
          throw new RuntimeException("Cannot find price");
        }
        plusDays++;
        date = date.plusDays(1);
      }
      value += stockPricesRecord.get(stock.getKey()).get(DateUtil.convertInt(date))
              * stock.getValue();
    }
    return value;
  }

}

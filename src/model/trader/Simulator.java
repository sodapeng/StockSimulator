package model.trader;

import util.DateUtil;
import util.PriceRecord;
import util.StockDataRetriever;
import util.WebStockDataRetriever;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    // for now use WebStockDataRetriever as default, can be expanded by passing this through constructor
    private StockDataRetriever dataRetriever = new WebStockDataRetriever();

    /**
     * Support command "[-simulate -run principle investingAmount startDate endDate DOLLARCOSTAVERAGE/OPTION2/OPTION3 MONTH/QUARTER {a list of stock proportion pairs}]"
     * @param principle       principle
     * @param investAmount    the amount of money used in each investment
     * @param startDate       simulation start date
     * @param endDate         simulation end date
     * @param strategy        simulation strategy
     * @param cadence         investment cadence
     * @param proportionMap   stock to proportion map, proprotions must add up to 1
     * @throws Exception
     */
    public Simulator(double principle, double investAmount, LocalDate startDate, LocalDate endDate, String strategy, String cadence, Map<String, Double> proportionMap) throws Exception {
        this.principle = principle;
        this.investAmount = investAmount;

        this.startDate = setStartDate(startDate);
        this.endDate = setEndDate(endDate);

        this.cadence = setCadance(cadence);

        this.proportionMap = setProportion(proportionMap);

        // get historical stock data for all stocks in the basket within the time range
        this.stockPricesRecord = fetchHistoricalStockPrices(this.proportionMap, this.startDate, this.endDate);

        this.strategy = setStrategy(strategy, this.proportionMap, this.stockPricesRecord, this.dataRetriever);


        Basket initialBasket = this.strategy.invest(initBasket(this.proportionMap), this.principle, this.startDate);
        basketSnapshots = new TreeMap<Integer, Basket>();
        basketSnapshots.put(DateUtil.convertInt(this.startDate), initialBasket);

        // run simulation
        simulate();
    }

    /**
     * Get profit at a given time.
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
            basketSnapshots.put(DateUtil.convertInt(current), newBasket);
            curBasket = newBasket;
        }
    }

    private LocalDate setStartDate(LocalDate startDate) {
        if (startDate.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new IllegalArgumentException("Start date must be later than 01/01/1900\n");
        }
        return startDate;
    }

    private LocalDate setEndDate(LocalDate endDate) {
        if (endDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("End date must be no later than today\n");
        }
        return endDate;
    }

    private InvestingStrategy setStrategy(String strategy, Map<String, Double> proportionMap, Map<String, Map<Integer, Double>> stockPricesRecord, StockDataRetriever dataRetriever) {
        if (strategy.equals("DCA")) {
            return new DollarCostAverageStrategy(proportionMap, stockPricesRecord, dataRetriever);
        } else if (strategy.equals("AR")) {
            return new DollarCostAverageStrategy(proportionMap, stockPricesRecord, dataRetriever);
        } else {
            throw new IllegalArgumentException("Unknown Investing Strategy\n");
        }
    }

    private String setCadance(String cadence) {
        if (!cadence.equals("MONTH") && !cadence.equals("QUARTER")) {
            throw new IllegalArgumentException("Unknown Cadence\n");
        }
        return cadence;
    }

    private Map<String, Double> setProportion(Map<String, Double> proportionMap) {
        double sum = 0;
        for (String stock : proportionMap.keySet()) {
            sum += proportionMap.get(stock);
        }
        if (sum - 1 < 0.00001) {
            return proportionMap;
        } else {
            throw new IllegalArgumentException("Stock proportion must add up to 1\n");
        }
    }

    private Basket initBasket(Map<String, Double> proportionMap) throws Exception {
      Basket basket = new Basket("SimulationBasket", this.dataRetriever, 19000101);
        for (String stock : proportionMap.keySet()) {
            basket.addStock(stock, 0);
        }
        return basket;
    }

    private Map<String, Map<Integer, Double>> fetchHistoricalStockPrices(Map<String, Double> proportionMap, LocalDate startDate, LocalDate endDate) throws Exception {
        Map<String, Map<Integer, Double>> stockPricesRecord = new HashMap<String, Map<Integer, Double>>();
        for (String stock : proportionMap.keySet()) {
            Map<Integer, PriceRecord> dateToPricRecordeMap = dataRetriever.getHistoricalPrices(stock,
                    startDate.getDayOfMonth(), startDate.getMonthValue(), startDate.getYear(),
                    endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear());
            Map<Integer, Double> dateToClosingPriceMap = new HashMap<Integer, Double>();
            for(int date : dateToPricRecordeMap.keySet()) {
                dateToClosingPriceMap.put(date, dateToPricRecordeMap.get(date).getClosePrice());
            }
            stockPricesRecord.put(stock, dateToClosingPriceMap);
        }
        return stockPricesRecord;
    }

    private double getBasketValue(LocalDate date) {
        double value = 0;
        Basket basket = basketSnapshots.floorEntry(DateUtil.convertInt(date)).getValue();
        for (Map.Entry<String, Integer> stock : basket.getStockMap().entrySet()) {
            // if the date looking for is not a business day, look for previous last business day
            while (stockPricesRecord.get(stock.getKey()).get(DateUtil.convertInt(date)) == null) {
                date = date.minusDays(1);
            }
            value += stockPricesRecord.get(stock.getKey()).get(DateUtil.convertInt(date)) * stock.getValue();
        }
        return value;
    }
}

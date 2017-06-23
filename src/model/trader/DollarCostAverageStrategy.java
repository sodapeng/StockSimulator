package model.trader;

import util.DateUtil;
import util.StockDataRetriever;

import java.time.LocalDate;
import java.util.Map;

public class DollarCostAverageStrategy implements InvestingStrategy {

    private Map<String, Double> proportion;

    // save historical prices data in memory to fetch it really fast
    private Map<String, Map<Integer, Double>> stockPricesRecord;

    private StockDataRetriever dataRetriever;

    /**
     * Initialize DollarCostAverageStrategy
     * @param proportion            stock to proportion map, proprotions must add up to 1
     * @param stockPricesRecord     historical stock prices data
     */
    public DollarCostAverageStrategy(Map<String, Double> proportion, Map<String, Map<Integer, Double>> stockPricesRecord, StockDataRetriever dataRetriever) {
        this.proportion = proportion;
        this.stockPricesRecord = stockPricesRecord;
        this.dataRetriever = dataRetriever;
    }

    /**
     * Invest certain amout of money at specific date to the basket
     * @param preBasket            the basket in simulation
     * @param investingAmount   a mount of money invested each time
     * @param date              investing date
     * @return                  a new basket after investment
     * @throws Exception
     */
    @Override
    public Basket invest(final Basket preBasket, double investingAmount, LocalDate date) throws Exception {
        // must use new instance each time to keep a history snapshot of basket
        Basket newBasket = new Basket("SimulationBasket", this.dataRetriever, DateUtil.convertInt(date));
        for (Map.Entry<String, Integer> stock : preBasket.getStockMap().entrySet()) {
            // if the date is not business day, invest on the last business day
            while (stockPricesRecord.get(stock.getKey()).get(DateUtil.convertInt(date)) == null) {
                date = date.minusDays(1);
            }
            int share = (int) Math.floor(investingAmount * proportion.get(stock.getKey()) / stockPricesRecord.get(stock.getKey()).get(DateUtil.convertInt(date)));
            newBasket.addStock(stock.getKey(), stock.getValue() + share);
        }
        return newBasket;
    }
}

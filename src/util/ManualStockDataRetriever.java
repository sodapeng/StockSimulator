package util;

import java.util.Map;

/**
 * This class represents a stock retriever module that can be implemented differently.
 * It is a singleton, and so to get the one (and only) object call getStockDataRetriever()
 */
public class ManualStockDataRetriever implements StockDataRetriever {

  public double getCurrentPrice(String stockSymbol) throws Exception {

    return 0.0;
  }

  public String getName(String stockSymbol) throws Exception {

    return null;
  }

  public Map<Integer, PriceRecord> getHistoricalPrices(
          String stockSymbol,
          int fromDate,
          int fromMonth,
          int fromYear,
          int toDate,
          int toMonth,
          int toYear)
          throws
          Exception {

    return null;
  }
}

package util;

/**
 * This class represents the record of a price of a single stock item in a day.
 */
public class PriceRecord {
  private final double open;
  private final double close;
  private final double highest;
  private final double lowest;

  /**
   * Construct a price record object.
   *
   * @param open    the open price
   * @param close   the close price
   * @param lowest  the lowest price
   * @param highest the highest price
   */
  public PriceRecord(double open, double close, double lowest, double highest) {
    this.open = open;
    this.close = close;
    this.highest = highest;
    this.lowest = lowest;
  }

  /**
   * Get the open price.
   *
   * @return open price
   */
  public double getOpenPrice() {
    return open;
  }

  /**
   * Get the close price.
   *
   * @return close price
   */
  public double getClosePrice() {
    return close;
  }

  /**
   * Get lowest price.
   *
   * @return lowest price
   */
  public double getLowestDayPrice() {
    return lowest;
  }

  /**
   * Get highest price.
   *
   * @return highest price
   */
  public double getHighestDayPrice() {
    return highest;
  }
}


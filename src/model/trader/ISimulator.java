package model.trader;

import java.time.LocalDate;

/**
 * This is ISimulator interface, it contains operations that all simulator should support.
 */
public interface ISimulator {

  /**
   * Get profit at a given time.
   *
   * @param date the date at which profit is computed.
   * @return the profit
   */
  double getProfit(LocalDate date);
}

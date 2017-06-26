package model.trader;

import java.time.LocalDate;

/**
 * This is an InvestingStrategy interface contains the operation that all types of investing
 * strategy should support.
 */

public interface InvestingStrategy {

  /**
   * Invest certain amount of money at specific date to the basket.
   *
   * @param basket          the basket in simulation
   * @param investingAmount a mount of money invested each time
   * @param date            investing date
   * @return a new basket after investment
   */
  Basket invest(final Basket basket, double investingAmount, LocalDate date) throws Exception;

  /**
   * Return the actual investment cost at each investment.
   *
   * @return the actual investment cost
   */
  double getInvestingCost();
}

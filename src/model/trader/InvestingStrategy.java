package model.trader;

import java.time.LocalDate;

public interface InvestingStrategy {

    /**
     * Invest certain amout of money at specific date to the basket
     * @param basket            the basket in simulation
     * @param investingAmount   a mount of money invested each time
     * @param date              investing date
     * @return                  a new basket after investment
     * @throws Exception
     */
    Basket invest(final Basket basket, double investingAmount, LocalDate date) throws Exception;
}

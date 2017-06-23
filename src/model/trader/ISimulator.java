package model.trader;

import java.time.LocalDate;

public interface ISimulator {

    /**
     * Get profit at a given time.
     * @param date the date at which profit is computed.
     * @return the profit
     */
    double getProfit(LocalDate date);
}

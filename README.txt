/**
 * Usage: (Examples see below)
 * Date Format : YYYYMMDD
 * [-create basketName createdate]: Create an empty basket with given name and creation date.
 *
 * [-add stockName share basketName]: Add one stock with corresponding share to an existing basket.
 *
 * [-print basketName]: Print the content (stocks and their shares) in existing basket.
 *
 * [-trend stockName startDate endDate]: Calculate one stock or basket price changing trend in given
 * date range.
 *
 * [-graph -blankgraph]: Generate a new empty graph. Add and Remove option then can be used 
 * to update this graph.
 *
 * [-graph -plotclosing basketname/stockname startDate endDate]: Generate a new graph with closing
 * price data. One or more stocks and baskets can be entered once. Add and remove option
 * then can be used to update this graph.
 *
 * [-graph -plotMA50 basketname/stockname startDate endDate]: Generate a new graph with 50 days
 * moving average and closing price data. One or more stocks and baskets can be entered
 * once. Add and remove option then can be used to update this graph.
 *
 * [-graph -plotMA200 basketname/stockname startDate endDate]: Generate a new graph with 200 days
 * moving average and closing price data. One or more stocks and baskets can be entered
 * once. Add and remove option then can be used to update this graph.
 *
 * [-graph -plotMA50-200 basketname/stockname startDate endDate]: Generate a new graph with 50 and
 * 200 days moving average and closing price data. One or more stocks and baskets can be
 * entered once. Add and remove option then can be used to update this graph.
 *
 * [-graph -add basketname/stockname startDate endDate]: Add closing price data with given stock
 * or basket name to current graph. One or more stocks and baskets can be entered once.
 *
 * [-graph -addMA days basketname/stockname startDate endDate]: Add moving average data given stock
 * or basket name to current graph. days represents X-Days moving average. One or more stocks
 * and baskets can be entered once.
 *
 * [-graph -remove basketname/stockname]: Remove a line in current graph. 
 *
 * [-simulate -run principle investingAmount startDate endDate DCA/AR MONTH/QUARTER {a list of stock proportion pairs}]
 * Run the simulation based on given principle (start investment money), investingAmount(money invest periodically),
 * start and end date of investiment. Stragedy that is going to be used: DCA: Dollar-cost averaging, AR: Automatic 
 * rebalancing. Time period interval for investment: MONTH: invest once a month, QUARTHER: invest once in four month.
 * A list of stock proportion pairs start with stock symbol, and followed with corresponding proportion. Proportion in
 * the list need to be added up to 1. If start date is not business day, last Friday will be used as start date. Then simulate query can be called. (See note 1,2 below)
 *
 * [-simulate -query date] get the profit of previous simulation, on the certain given query date. Query date need to 
 * in range of previous investment date range. Query is available after simulation runs. If query date is not business
 * day, last Friday will be used as query date.
 *
 * [q Exit]: Exit the program.
 *
 * 
 * Note1: Simulation on previous created basket is not supported due to not enough informtion
 * Note2: Profit is based on actual initial principle, plus money that is invested periodically.
 * Share of each stocks in an basket is calculated by proportion * principle / stock price. Then
 * the share will be casted in to integer as final share of this stock in this basket. Then the
 * actual initial principle will be calculated by each stocks' prices * its share.)

 /**
   * Examples:
   * [-create basketName createdate]: -create basket1 20160601
   * 
   * [-add stockName share basketName]: -add AAPL 10 basket1
   * 
   * [-print basketName]: -print basket1
   * 
   * [-trend stockName startDate endDate]: -trend basket1 20160601 20170601
   * 
   * [-graph -blankgraph]: -graph -blankgraph
   * 
   * [-graph -plotclosing basketname/stockname startDate endDate]: -graph -plotclosing basket1 AMZN
   * 20160601 20170601
   * 
   * [-graph -plotMA50 basketname/stockname startDate endDate]: -graph -plotMA50 basket1 GOOGL AMZN
   * 20160601 20170601
   * 
   * [-graph -plotMA200 basketname/stockname startDate endDate]: -graph -plotMA200 basket1 GOOGL AMZN
   * 20160601 20170601
   * 
   * [-graph -plotMA50-200 basketname/stockname startDate endDate]: -graph -plotMA50-200 basket1 GOOGL AMZN
   * 20160601 20170601
   * 
   * [-graph -add basketname/stockname startDate endDate]: -graph -add AMZN 20160601 20170601
   * 
   * [-graph -addMA days basketname/stockname startDate endDate]: -graph -addMA 50 MSFT 20160601 20170601
   * 
   * [-graph -remove basketname/stockname]: -graph -remove basket1
   * 
   * [-simulate -run principle investingAmount startDate endDate DCA/AR MONTH/QUARTER {a list of stock 
   * proportion pairs}]
   * -simulate -run 5000 1000 20160601 20170601 DCA MONTH AMZN 0.5 GOOGL 0.5
   * 
   * [-simulate -query date]: -simulate -query 20170531
   */
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.trader.IStockModel;
import model.trader.TrendCalculator;
import util.StockDataRetriever;
import view.trader.InteractiveView;

/**
 * This is a Interactive Controller class, it takes in and process user input, and transfer data
 * to model, and then get back data and send to view for proper display.
 * Different actions, include model here.
 */
public class InteractiveController implements Controller {

  private InteractiveView view;
  private final Readable in;

  private StockDataRetriever dataRetriever;
  private TrendCalculator trendCalculator;
  private IStockModel iStockModel;

  /**
   * Construct an Interactive controller object.
   * @param in input
   * @param view view
   * @param iStockModel a stock set
   * @param dataRetriever stock data retriever
   * @param trendCalculator trend calculator
   * @throws Exception when cannot retrieve data
   */
  public InteractiveController(Readable in, InteractiveView view,
                               IStockModel iStockModel, StockDataRetriever dataRetriever,
                               TrendCalculator trendCalculator) throws Exception {
    this.in = in;
    this.view = view;
    this.dataRetriever = dataRetriever;
    this.trendCalculator = trendCalculator;
    this.iStockModel = iStockModel;
  }

  /**
   * Start program, catch exceptions throwed by model.
   */
  public void startProgram() throws Exception {
    // parse the arguments and figure out which controller action to take
    Scanner sc = new Scanner(this.in);
    while (true) {
      //print the manual
      view.manual();
      String input = sc.nextLine();
      // quit
      if (input.equals("q")) {
        view.exit("Exit!");
        return;
      } else {
        // split input args on whitespaces
        String[] inputArgs = input.split("\\s+");
        processArgs(inputArgs);
      }
    }
  }

  /**
   * Process user input.
   * Catch exception if input is invalid.
   * @param args user input
   */
  private void processArgs(String[] args) throws Exception {

    if (args[0].equals("-create") && args.length > 2) {
      try {
        Integer.parseInt(args[2]);
        createBasket(args[1], Integer.parseInt(args[2]));
      } catch (NumberFormatException e) {
        view.printError("create date must be integer");
      }
    } else if (args[0].equals("-add") && args.length > 3) {
      try {
        Integer.parseInt(args[2]);
        addStockToBasket(args[1], Integer.parseInt(args[2]), args[3]);
      } catch (NumberFormatException e) {
        view.printError("share must be an integer\n");
      } catch (IllegalArgumentException e) {
        view.printError("invalid input\n");
      }
    } else if (args[0].equals("-print") && args.length > 1) {
      printBasket(args[1]);
    } else if (args[0].equals("-trend") && args.length > 3) {
      try {
        int start = Integer.parseInt(args[2]);
        int end = Integer.parseInt(args[3]);
        trend(args[1], start, end, trendCalculator);
      } catch (NumberFormatException e) {
        view.printError("start and end date must be an integer\n");
      }
    } else if (args[0].equals("-graph")) {
      processGraphArgs(args);
    } else {
      view.printError("invalid input\n");
    }
  }

  /**
   * Process graph accordingly to user input.
   *
   * @param args user input
   */
  private void processGraphArgs(String[] args) throws Exception {
    if (args[1].equals("-plotclosing") && args.length >= 5) {
      plotclosing(args);
    } else if (args[1].equals("-blankgraph")) {
      plotblank();
    } else if (args[1].equals("-plotMA50") && args.length >= 5) {
      plotmoving(args, 50);
    } else if (args[1].equals("-plotMA200") && args.length >= 5) {
      plotmoving(args, 200);
    } else if (args[1].equals("-plotMA50-200") && args.length >= 5) {
      plotmoving(args, 250);
    } else if (args[1].equals("-add") && args.length >= 5) {
      plotadd(args);
    } else if (args[1].equals("-remove") && args.length >= 3) {
      plotremove(args);
    } else if (args[1].equals("-addMA") && args.length >= 6) {
      plotaddmovave(args);
    } else {
      view.printError("invalid input\n");
    }
  }

  /**
   * Tell view to create a blank graph.
   */
  private void plotblank() throws IOException {
    view.printGraph();
    view.printMessage("blank graph created\n");
  }

  /**
   * Retrieve closing price data form model.
   * Send data to view to generate a new graph.
   * Catch exception when date input cannot be parsed into integer.
   * @param args data need to be retrieved and plotted.
   */
  private void plotclosing(String[] args) throws Exception {
    Map<String, Map<Integer, Double>> dataTotal = new HashMap<>();
    try {
      int end = Integer.parseInt(args[args.length - 1]);
      int start = Integer.parseInt(args[args.length - 2]);
      for (int i = 2; i < args.length - 2; i++) {
        Map<String, Map<Integer, Double>> data = iStockModel.iStockPlot(args[i], start, end);
        dataTotal.putAll(data);
      }
      view.plotWithData(dataTotal);
      view.printMessage("Historical closing price data plotted\n");
    } catch (NumberFormatException e) {
      view.printError("from date and end date must be integer\n");
    } catch (IndexOutOfBoundsException e) {
      view.printError("Maximum 11 lines are allowed in one graph\n");
    }
  }

  /**
   * Retrieve moving average data and closing price data form model.
   * Send data to view to generate a new graph.
   * Catch exception when date input cannot be parsed into integer.
   * @param args data need to be retrieved and plotted.
   */
  private void plotmoving(String[] args, int days) throws Exception {
    Map<String, Map<Integer, Double>> dataTotal = new HashMap<>();
    try {
      int end = Integer.parseInt(args[args.length - 1]);
      int start = Integer.parseInt(args[args.length - 2]);
      for (int i = 2; i < args.length - 2; i++) {
        Map<String, Map<Integer, Double>> closingdata = iStockModel.iStockPlot(args[i], start, end);
        Map<String, Map<Integer, Double>> data = iStockModel.iStockPlotMov(args[i], start, end, days);
        dataTotal.putAll(closingdata);
        dataTotal.putAll(data);
      }
      view.plotWithData(dataTotal);
      view.printMessage("moving average data printed\n");
    }
    catch (NumberFormatException e) {
      view.printError("from date and end date must be integer\n");
    } catch (IndexOutOfBoundsException e) {
      view.printError("Maximum 11 lines are allowed in one graph\n");
    }
  }

  /**
   * Remove line(s) from current graph.
   *
   * @param args lines / data need to be removed
   */
  private void plotremove(String[] args) throws IOException {
    ArrayList<String> removelines = new ArrayList<>();
    for (int i = 2; i < args.length; i++) {
      removelines.add(args[i]);
    }
    view.removeline(removelines);
  }

  /**
   * Retrieve closing data from model, and send back data to view.
   * Add closing data to current graph.
   *
   * @param args data need to be retrieved and added
   * @throws Exception when cannot retrieve data
   */
  private void plotadd(String[] args) throws Exception {
    Map<String, Map<Integer, Double>> dataTotal = new HashMap<>();
    try {
      int end = Integer.parseInt(args[args.length - 1]);
      int start = Integer.parseInt(args[args.length - 2]);
      for (int i = 2; i < args.length - 2; i++) {
        Map<String, Map<Integer, Double>> data = iStockModel.iStockPlot(args[i], start, end);
        dataTotal.putAll(data);
      }
      view.addline(dataTotal);
    } catch (NumberFormatException e) {
      view.printError("from date and end date must be integer\n");
    }
  }

  /**
   * Retrieve moving average data from model, and send back data to view.
   * Add closing data to current graph.
   *
   * @param args data need to be retrieved and added
   * @throws Exception when cannot retrieve data
   */
  private void plotaddmovave(String[] args) throws Exception {
    Map<String, Map<Integer, Double>> dataTotal = new HashMap<>();
    try {
      int end = Integer.parseInt(args[args.length - 1]);
      int start = Integer.parseInt(args[args.length - 2]);
      int days = Integer.parseInt(args[2]);
      for (int i = 3; i < args.length - 2; i++) {
        Map<String, Map<Integer, Double>> closingdata = iStockModel.iStockPlot(args[i], start, end);
        Map<String, Map<Integer, Double>> data = iStockModel.iStockPlotMov(args[i], start, end, days);
        dataTotal.putAll(data);
        dataTotal.putAll(closingdata);
      }
      view.addline(dataTotal);
    } catch (NumberFormatException e) {
      view.printError("from date and end date must be integer\n");
    }
  }

  /**
   * Create a new basket with basket name and create date.
   * Send basket created successfully message to view.
   * @param basketName basket name
   * @param createDate basket create data
   *
   */
  private void createBasket(String basketName, int createDate) throws IOException {
    iStockModel.add(basketName, dataRetriever, createDate);
    view.printMessage("Basket " + basketName + " Created\n");
  }

  /**
   * Add stock to an existing basket
   * Send stock added message successfully to view if stock added successfully.
   * Send error message to view if basket has not been created yet.
   * @param stockName  stock name
   * @param share      number of shares
   * @param basketName basket name
   * @return true if stock was added
   */
  private void addStockToBasket(String stockName, int share, String basketName) throws Exception {
    if (!iStockModel.containsBasket(basketName)) {
      view.printError("basket " + basketName + " has not been created yet.\n");
      return;
    }
    iStockModel.addStock(basketName, stockName, share);
    view.printMessage("Stock " + stockName + " with share " + share + " has been added to "
            + basketName + "\n");
  }

  /**
   * Print the contents and value of a basket.
   * Send basket information to view if basket is found.
   * Send error message to view if basket has not been created yet.
   * @param basketName basket name
   */
  private void printBasket(String basketName) throws IOException {
    if (!iStockModel.containsBasket(basketName)) {
      view.printMessage("basket " + basketName + " has not been created yet\n");
      return;
    }
    view.printBasket(iStockModel.getBasketSet(), basketName);
  }

  /**
   * Get trend of a stock within a date range.
   * Send trend information to view, and if input is invalid, send error message.
   * @param stockName stock name
   * @param start     start date of the trend
   * @param end       end date of the trend
   * @return trend of stock from start date to end date
   */
  private void trend(String stockName, Integer start, Integer end, TrendCalculator trendCalculator)
          throws Exception {
    try {
      String trend = iStockModel.trend(stockName, start, end, trendCalculator);
      view.printMessage(trend + "\n");
    } catch (IllegalArgumentException e) {
      view.printError("invalid input\n");
    }
  }
}

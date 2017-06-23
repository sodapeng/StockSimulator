package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import model.trader.IStockModel;
import model.trader.SimpleTrendCalculator;
import model.trader.TrendCalculator;
import util.StockDataRetriever;
import util.WebStockDataRetriever;
import view.trader.InteractiveView;

import static org.junit.Assert.assertEquals;


/**
 * Created by zhuangmira on 6/16/17.
 */
public class InteractiveControllerTest {
  StockDataRetriever dataRetriever = new WebStockDataRetriever();
  IStockModel iStockModel = new IStockModel(dataRetriever);
  TrendCalculator trendCalculator = new SimpleTrendCalculator();
  Readable in;
  Appendable out = new StringBuilder();
  InteractiveView view = new InteractiveView(out);
  String manual;
  String exit;

  /**
   * Set up for InteractiveView test.
   */
  @Before
  public void setUp() throws IOException {
    manual = "Date Format : YYYYMMDD\n" + "[-create basketName createdate]\n"
            + "[-add stockName share basketName]\n[-print basketName]\n"
            + "[-trend stockName startDate endDate]\n" + "[-graph -blankgraph]\n"
            + "[-graph -plotclosing basketname/stockname startDate endDate]\n"
            + "[-graph -plotMA50 basketname/stockname startDate endDate]\n"
            + "[-graph -plotMA200 basketname/stockname startDate endDate]\n"
            + "[-graph -plotMA50-200 basketname/stockname startDate endDate]\n"
            + "[-graph -add basketname/stockname startDate endDate]\n"
            + "[-graph -addMA days basketname/stockname startDate endDate]\n"
            + "[-graph -remove basketname/stockname]\n" + "[q Exit]\n";
    exit = "Exit!";

  }

  /**
   * Test manual and exit print out correctly.
   */
  @Test
  public void manualExitTest() throws Exception {
    in = new StringReader("q\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + exit, view.toString());
  }

  /**
   * Test create basket print out correctly.
   */
  @Test
  public void createBasket() throws Exception {
    in = new StringReader("-create basket1 20150101\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Basket basket1 Created\n" + manual + exit, view.toString());
  }

  /**
   * Test add stock to basket print out correctly.
   */
  @Test
  public void addStockToBasket() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG 10 basket1\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Basket basket1 Created\n"
            + manual + "Stock GOOG with share 10 has been added to basket1\n"
            + manual + exit, view.toString());
  }

  /**
   * Test add stock to basket print out correctly when user give a non-integer as share.
   */
  @Test
  public void addStockInvalid() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG AS basket1\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Basket basket1 Created\n"
            + manual + "share must be an integer\n"
            + manual + exit, view.toString());
  }

  /**
   * Test add stock to basket print out correctly when user add stock to an un-exist basket.
   */
  @Test
  public void addStockInvalid2() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG 10 basket2\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Basket basket1 Created\n"
            + manual + "basket basket2 has not been created yet.\n"
            + manual + exit, view.toString());
  }

  /**
   * Test add stock to basket print out correctly when user input invalid stock symbol.
   */
  @Test
  public void addStockInvalid3() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG2 10 basket1\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Basket basket1 Created\n"
            + manual + "invalid input\n"
            + manual + exit, view.toString());
  }

  /**
   * Test -print basket print out correctly.
   */
  @Test
  public void printBasketTest() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG 10 basket1\n-add AAPL 5" +
            " basket1\n-print basket1\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    StringBuilder res = new StringBuilder(manual + "Basket basket1 Created\n"
            + manual + "Stock GOOG with share 10 has been added to basket1\n"
            + manual + "Stock AAPL with share 5 has been added to basket1\n" + manual);
    assertEquals(res + "print basket: \n" +
            "stock symbol: GOOGstock share: 10stock symbol: AAPLstock share: 5\n" +
            manual + exit, view.toString());
  }

  /**
   * Test print basket print out correctly when user input an un-exist basket.
   */
  @Test
  public void printBasketTest2() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG 10 basket1\n-add AAPL 5 basket1"
            + "\n-print basket2\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    StringBuilder res = new StringBuilder(manual + "Basket basket1 Created\n"
            + manual + "Stock GOOG with share 10 has been added to basket1\n"
            + manual + "Stock AAPL with share 5 has been added to basket1\n" + manual);
    assertEquals(res + "basket basket2 has not been created yet\n" + manual + exit,
            view.toString());
  }

  /**
   * Test trend print out correctly.
   */
  @Test
  public void trendTest() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG 10 basket1\n-add AAPL "
            + "5 basket1\n-trend basket1 20170601 20170617\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    StringBuilder res = new StringBuilder(manual + "Basket basket1 Created\n"
            + manual + "Stock GOOG with share 10 has been added to basket1\n"
            + manual + "Stock AAPL with share 5 has been added to basket1\n" + manual);
    assertEquals(res + "Strong decline\n" + manual + exit, view.toString());

  }

  /**
   * Test trend printout correctly when user input non-integer as date range.
   */
  @Test
  public void trendTest2() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG 10 basket1\n-add AAPL "
            + "5 basket1\n-trend basket2 asas asdas\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    StringBuilder res = new StringBuilder(manual + "Basket basket1 Created\n"
            + manual + "Stock GOOG with share 10 has been added to basket1\n"
            + manual + "Stock AAPL with share 5 has been added to basket1\n" + manual);
    assertEquals(res + "start and end date must be an integer\n" + manual + exit,
            view.toString());
  }

  /**
   * Test trend printout correctly when user input only one date as date range.
   */
  @Test
  public void trendTest3() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG 10 basket1\n-add AAPL 5 "
            + "basket1\n-trend basket2 20170512\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    StringBuilder res = new StringBuilder(manual + "Basket basket1 Created\n"
            + manual + "Stock GOOG with share 10 has been added to basket1\n"
            + manual + "Stock AAPL with share 5 has been added to basket1\n" + manual);
    assertEquals(res + "invalid input\n" + manual + exit, view.toString());
  }

  /**
   * Test trend printout correctly when user input a invalid date range.
   */
  @Test
  public void trendTest4() throws Exception {
    in = new StringReader("-create basket1 20150101\n-add GOOG 10 basket1\n-add"
            + " AAPL 5 basket1\n-trend basket2 20170512 20170501\nq\n");
    InteractiveController controller = new InteractiveController(in, view,
            iStockModel, dataRetriever, trendCalculator);
    controller.startProgram();
    StringBuilder res = new StringBuilder(manual + "Basket basket1 Created\n"
            + manual + "Stock GOOG with share 10 has been added to basket1\n"
            + manual + "Stock AAPL with share 5 has been added to basket1\n" + manual);
    assertEquals(res + "invalid input\n" + manual + exit, view.toString());
  }

  /**
   * Test plot blank graph printout correctly.
   */
  @Test
  public void plotBlankTest() throws Exception {
    in = new StringReader("-graph -blankgraph\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "blank graph created\n" + manual + exit, view.toString());
  }

  /**
   * Test plot closing print out correctly.
   */
  @Test
  public void plotClosingTest() throws Exception {
    in = new StringReader("-graph -plotclosing AAPL 20170601 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Historical closing price data plotted\n" + manual
            + exit, view.toString());
  }


  /**
   * Test plot closing print out correctly when user have invalid input.
   */
  @Test
  public void plotClosingTest1() throws Exception {
    in = new StringReader("-graph -plotclosing AAPL asa 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "from date and end date must be integer\n" +
            manual + exit, view.toString());
  }

  /**
   * Test plot closing print out correctly when user reach maximum lines allowed in one graph.
   */
  @Test
  public void plotClosingTest2() throws Exception {
    in = new StringReader("-graph -plotclosing AAPL MSFT GOOGL AMZN FB HEES HRB FUL HAE "
            + "HASI HQCL 20170601 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Maximum 11 lines are allowed in one graph\n" +
            manual + exit, view.toString());
  }


  /**
   * Test plot moving average print out correctly.
   */
  @Test
  public void plotMATest1() throws Exception {
    in = new StringReader("-graph -plotMA50 AAPL 20170601 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "moving average data printed\n" + manual + exit, view.toString());
  }

  /**
   * Test plot moving average print out correctly when user have invalid input.
   */
  @Test
  public void plotMATest2() throws Exception {
    in = new StringReader("-graph -plotMA50 AMZN 20170601 20170602\n-graph "
            + "-plotMA50 AAPL asa 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "moving average data printed\n" + manual
            + "from date and end date must be integer\n" + manual + exit, view.toString());
  }

  /**
   * Test plot moving average print out correctly when user reach maximum lines allowed in one
   * graph.
   */
  @Test
  public void plotMATest3() throws Exception {
    in = new StringReader("-graph -plotMA50 AAPL MSFT GOOGL AMZN FB HEES HRB FUL "
            + "HAE HASI HQCL 20170601 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Maximum 11 lines are allowed in one graph\n" + manual
            + exit, view.toString());
  }


  /**
   * Test remove line print out correctly.
   */
  @Test
  public void removeLineTest() throws Exception {
    in = new StringReader("-graph -plotclosing AAPL 20170601 20170602\n-graph"
            + " -remove AAPL\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Historical closing price data plotted\n" + manual
            + "line removed\n" + manual + exit, view.toString());
  }

  /**
   * Test remove line print out correctly when input line is not in the graph.
   */
  @Test
  public void removeLineTest1() throws Exception {
    in = new StringReader("-graph -plotclosing AAPL 20170601 20170602\n-graph"
            + " -remove AAPL12\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Historical closing price data plotted\n" + manual
            + "line is not exist\n" + manual + exit, view.toString());
  }


  /**
   * Test add moving average print out correctly.
   */
  @Test
  public void addMATest() throws Exception {
    in = new StringReader("-graph -plotMA50 AMZN 20170601 20170602\n-graph"
            + " -addMA 200 AAPL 20170601 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "moving average data printed\n" + manual
            + "data added to graph\n" + manual + exit, view.toString());
  }

  /**
   * Test plot moving average print out correctly when user reach maximum lines
   * allowed in one graph.
   */
  @Test
  public void addMATest1() throws Exception {
    in = new StringReader("-graph -plotclosing AAPL MSFT GOOGL AMZN FB HEES HRB FUL "
            + "HAE HASI 20170601 20170602\n-graph -addMA 50 AAPL 20170601 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "Historical closing price data plotted\n" + manual
            + "Maximum 11 lines are allowed in one graph\n" + manual + exit, view.toString());
  }


  /**
   * Test add closing price print out correctly.
   */
  @Test
  public void addClosingTest() throws Exception {
    in = new StringReader("-graph -plotMA50 AMZN 20170601 20170602\n"
            + "-graph -add AAPL 20170601 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "moving average data printed\n" + manual
            + "data added to graph\n" + manual + exit, view.toString());
  }


  /**
   * Test add closing price print out correctly when new graph has not been created yet.
   */
  @Test
  public void addClosingTest2() throws Exception {
    in = new StringReader("-graph -add AAPL 20170601 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "create a graph first\n" + manual + exit, view.toString());
  }

  /**
   * Test add moving average print out correctly when new graph has not been created yet.
   */
  @Test
  public void addMATest2() throws Exception {
    in = new StringReader("-graph -addMA 50 AAPL 20170601 20170602\nq\n");
    InteractiveController controller = new InteractiveController(in, view, iStockModel,
            dataRetriever, trendCalculator);
    controller.startProgram();
    assertEquals(manual + "create a graph first\n" + manual + exit, view.toString());
  }

}
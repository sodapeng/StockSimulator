package view.trader;

import java.io.IOException;
import java.io.InputStreamReader;

import controller.InteractiveController;
import model.trader.IStockModel;
import model.trader.SimpleTrendCalculator;
import model.trader.TrendCalculator;
import util.StockDataRetriever;
import util.WebStockDataRetriever;

/**
 * This is an App class has only one main method as program entrance.
 */

public class App {

  /**
   * Entrance to the application.
   *
   * @param args input arguments
   */
  public static void main(String[] args) throws IOException {
    StockDataRetriever dataRetriever = new WebStockDataRetriever();
    IStockModel iStockModel = new IStockModel(dataRetriever);
    TrendCalculator trendCalculator = new SimpleTrendCalculator();

    final Readable IN = new InputStreamReader(System.in);
    final Appendable OUT = System.out;
    InteractiveView view = new InteractiveView(OUT);

    try {
      //give controller the model and view
      InteractiveController controller = new InteractiveController(IN, view, iStockModel,
              dataRetriever, trendCalculator);
      //give control to the controller. Controller relinquishes only when program ends
      controller.startProgram();

    } catch (Exception e) {
      view.printError("invalid input\n");
    }
  }

}

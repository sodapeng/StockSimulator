package view.trader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import model.trader.Basket;
import util.DrawLine;

/**
 * This is an Interactive View class, represent the view part in MVC.
 * It receives message/data from controller, and then decided to method/way to print it.
 */
public class InteractiveView implements TextView, GraphicView {


  private final Appendable out;
  private DrawLine graph;

  /**
   * Construct an interactive view object.
   *
   * @param out out
   */
  public InteractiveView(Appendable out) {
    this.out = out;
  }

  @Override
  public void manual() throws IOException {
    this.out.append("Date Format : YYYYMMDD\n" + "[-create basketName createdate]\n"
            + "[-add stockName share basketName]\n[-print basketName]\n"
            + "[-trend stockName startDate endDate]\n" + "[-graph -blankgraph]\n"
            + "[-graph -plotclosing basketname/stockname startDate endDate]\n"
            + "[-graph -plotMA50 basketname/stockname startDate endDate]\n"
            + "[-graph -plotMA200 basketname/stockname startDate endDate]\n"
            + "[-graph -plotMA50-200 basketname/stockname startDate endDate]\n"
            + "[-graph -add basketname/stockname startDate endDate]\n"
            + "[-graph -addMA days basketname/stockname startDate endDate]\n"
            + "[-graph -remove basketname/stockname]\n"
            + "[-simulate -run principle investingAmount startDate endDate "
            + "DCA/AR MONTH/QUARTER {a list of stock proportion pairs}]\n"
            + "[-simulate -query date]\n"
            + "[q Exit]\n");
  }

  @Override
  public void printBasket(Map<String, Basket> map, String basketname) throws IOException {
    if (!map.containsKey(basketname)) {
      this.out.append("basket " + basketname + " has not been created yet.");
    }
    this.out.append("print basket: " + "\n" + map.get(basketname).toString() + "\n");
  }


  @Override
  public void printError(String message) throws IOException {
    this.out.append(message);
  }

  @Override
  public void printMessage(String message) throws IOException {
    this.out.append(message);
  }


  @Override
  public void exit(String message) throws IOException {
    this.out.append(message);
  }


  @Override
  public void printGraph() throws IOException {
    graph = new DrawLine();
  }

  @Override
  public void addline(Map<String, Map<Integer, Double>> newdata) throws IOException {
    if (graph == null) {
      this.out.append("create a graph first\n");
      return;
    }
    if (graph.getGraphData().size() + newdata.size() < 11) {
      graph.addStock(newdata);
      this.out.append("data added to graph\n");
    } else {
      this.out.append("Maximum 11 lines are allowed in one graph\n");
    }

  }


  @Override
  public void removeline(List<String> istocklist) throws IOException {
    if (graph == null) {
      this.out.append("create a graph first\n");
      return;
    }
    Map<String, Map<Integer, Double>> graphcopy = graph.getGraphData();
    for (String stockname : istocklist) {
      System.out.println("stock name : " + stockname);
      if (graphcopy.containsKey(stockname)) {

        graph.removeStock(stockname);
        graphcopy.remove(stockname);
      } else {
        this.out.append("line is not exist\n");
        return;
      }
    }
    this.out.append("line removed\n");
  }


  @Override
  public void plotWithData(Map<String, Map<Integer, Double>> newdata) throws IOException {
    graph = new DrawLine();
    if (newdata.size() < 11) {
      graph.addStock(newdata);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public String toString() {
    return this.out.toString();
  }


}

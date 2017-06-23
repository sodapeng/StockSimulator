package view.trader;

import java.io.IOException;
import java.util.Map;


/**
 * This is a GraphicView interface represent an interactive graphic view.
 * It contains the operation that support interactive graphic view.
 */
public interface GraphicView {

  /**
   * Create a new blank graph.
   */
  void printGraph() throws IOException;

  /**
   * Add a line to current graph with given data.
   * Maximum 11 lines/data will be allowed in one graph, if data size exceeds range, only 11 of
   * them will be plotted.
   * New graph need to be created before add new data.
   *
   * @param newdata given new data
   */
  void addline(Map<String, Map<Integer, Double>> newdata) throws IOException;

  /**
   * Remove a line from current graph with given IStock name.
   * New graph need to be created before remove data.
   *
   * @param istocklist a list of IStock name
   */
  void removeline(java.util.List<String> istocklist) throws IOException;

  /**
   * Create a new graph with given data.
   * Maximum 11 lines/data will be allowed in one graph, if data size exceeds range, only 11 of
   * them will be plotted.
   *
   * @param newdata data for plot
   */
  void plotWithData(Map<String, Map<Integer, Double>> newdata) throws IOException;
}

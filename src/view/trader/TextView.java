package view.trader;

import java.io.IOException;
import java.util.Map;

import model.trader.Basket;

/**
 * This is a TextView interface represent an interactive text view.
 * It contains the operation that support interactive text view.
 */
public interface TextView {

  /**
   * Print manual of the app.
   */
  void manual() throws IOException;

  /**
   * Print error message.
   *
   * @param message error message
   */
  void printError(String message) throws IOException;

  /**
   * Print message.
   *
   * @param message error message
   */
  void printMessage(String message) throws IOException;

  /**
   * Print the program exit message to user.
   *
   * @param message exit message
   */
  void exit(String message) throws IOException;

  /**
   * Print Basket with given information.
   *
   * @param map        the basket information
   * @param basketname name of given basket
   */
  void printBasket(Map<String, Basket> map, String basketname) throws IOException;
}

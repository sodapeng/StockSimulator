package controller;

/**
 * This is a controller interface represents the controller of this program.
 * It has a startProgram operation that kicks off the program.
 */
public interface Controller {

  /**
   * Start program, catch exceptions throwed by model.
   */
  void startProgram() throws Exception;
}

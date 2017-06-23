package view.trader;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by zhuangmira on 6/16/17.
 */
public class InteractiveViewTest {
  private InteractiveView view;
  private String manual;
  private String exit;

  /**
   * Set up for InteractiveView test.
   */
  @Before
  public void setUp() {
    view = new InteractiveView(new StringBuilder());
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

  @Test
  public void manualTest() throws Exception {
    view.manual();
    assertEquals(manual, view.toString());
  }

  @Test
  public void exitTest() throws Exception {
    view.exit("Exit!");
    assertEquals(exit, view.toString());
  }

  @Test
  public void printMessage() throws Exception {
    view.printMessage("print message");
    assertEquals("print message", view.toString());
  }

  @Test
  public void printError() throws Exception {
    view.printMessage("print error");
    assertEquals("print error", view.toString());
  }


}
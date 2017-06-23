package util;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.trader.IStock;

/**
 * This is a Drawline class for graph generation.
 */
public class DrawLine extends JFrame {
  private DrawPanel drawPanel;

  /**
   * This is a drawpanel class for panel generation.
   */
  private class DrawPanel extends JPanel {

    private Map<String, Map<Integer, Double>> data;
    private List<IStock> istocks;
    private final int PAD = 40;
    private final Color[] colorarray = {Color.BLACK, Color.BLUE, Color.CYAN,
            Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK,
            Color.RED, Color.YELLOW};

    /**
     * Construct a draw panel object.
     */
    public DrawPanel() {
      super();
      //set background to white
      this.setBackground(Color.WHITE);
      this.data = new HashMap<>();
      this.istocks = istocks;
    }

    /**
     * Get current data in the graph.
     *
     * @return current data in the graph
     */
    public Map<String, Map<Integer, Double>> getCurGraphData() {
      return new HashMap<String, Map<Integer, Double>>(data);
    }

    /**
     * Add new data to current graph.
     *
     * @param newdata need to be added
     */
    public void add(Map<String, Map<Integer, Double>> newdata) {
      data.putAll(newdata);
    }

    /**
     * Remove data from current graph.
     *
     * @param istockname date need to be removed
     */
    public void remove(String istockname) {
      data.remove(istockname);
    }


    @Override
    public Dimension getPreferredSize() {
      return new Dimension(600, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {

      super.paintComponent(g);

      int w = getWidth();
      int h = getHeight();
      Graphics2D g2d = (Graphics2D) g;

      g2d.setColor(Color.BLACK);

      //draw the coordinate.
      g2d.drawLine(PAD, PAD, PAD, h - PAD);
      g2d.drawLine(PAD, h - PAD, w - PAD, h - PAD);
      g2d.drawString("Date", w - PAD, h - PAD);
      g2d.drawString("Price", 0, PAD);


      //scale the x-axis and y-axis based on given data.
      int xinterval = (w - 2 * PAD) / (getXMax());
      double yinterval = (double) (h - 2 * PAD - 100) / getYMax();


      int j = 0;
      for (Map.Entry<String, Map<Integer, Double>> e : data.entrySet()) {

        g2d.setColor(colorarray[j]);

        List<Integer> sortedkeys = new ArrayList<>(e.getValue().keySet());

        Collections.sort(sortedkeys);
        int pre = sortedkeys.get(0);
        int prex = 0;
        double prey = 0;
        long startpointValue = Math.round(e.getValue().get(sortedkeys.get(0)));

        //draw the stock name and start point price as graph symbol.
        g2d.drawString(e.getKey() + ": start point price: " + startpointValue,
                PAD, (PAD / 2) * (j + 1));

        //traverse each data point in the map, and draw it on the graph.
        for (int i = 0; i < sortedkeys.size(); i++) {
          int diff = DateUtil.timeDiff(sortedkeys.get(i), pre);
          int x = PAD + diff + i * xinterval;
          double y = h - PAD - e.getValue().get(sortedkeys.get(i)) * yinterval;
          g2d.fill(new Ellipse2D.Double(x, y, 4, 4));

          pre = sortedkeys.get(i);
          if (prex != 0) {
            //draw a line between each single data point.
            g2d.draw(new Line2D.Double(x, y, prex, prey));
          }
          prex = x;
          prey = y;
        }
        j++;
      }
    }

    /**
     * Get the maximum x-value in the data for plot scale.
     *
     * @return the maximum x-value in the data
     */
    private int getXMax() {
      int max = Integer.MIN_VALUE;
      for (Map<Integer, Double> map : data.values()) {
        max = Math.max(map.size(), max);
      }
      return max;
    }

    /**
     * Get the maximum y-value in the data for plot scale.
     *
     * @return the maximum y-value in the data
     */
    private double getYMax() {
      double max = 0;
      for (Map<Integer, Double> map : data.values()) {
        Collection<Double> values = map.values();
        for (double value : values) {
          max = Math.max(max, value);
        }
      }
      return max;
    }


  }

  /**
   * Construct a drawline object.
   */
  public DrawLine() {
    //call the constructor of JFrame, let it do what it does.
    super();
    //the X button should close this window, but not the entire program
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //add the panel to this frame
    drawPanel = new DrawPanel();
    this.add(drawPanel);
    //resize this frame so that it is just big enough to hold the panel
    //the panel sets its own size by overriding getPreferredSize
    this.pack();
    //make the window visible. By default a window is invisible.
    this.setVisible(true);
  }

  /**
   * Add data to current graph, and refresh the screen.
   *
   * @param newdata to be added
   */
  public void addStock(Map<String, Map<Integer, Double>> newdata) {
    drawPanel.add(newdata);
    repaint(); //refresh the screen
  }

  /**
   * Remove data from current graph, and refresh the screen.
   *
   * @param istock to be removed
   */
  public void removeStock(String istock) {
    drawPanel.remove(istock);
    repaint(); //refresh the screen
  }

  /**
   * Get the data in current graph.
   *
   * @return data in current graph
   */
  public Map<String, Map<Integer, Double>> getGraphData() {
    return drawPanel.getCurGraphData();
  }
}
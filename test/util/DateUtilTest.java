package util;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;


import static org.junit.Assert.assertEquals;

/**
 * This is a JUnit test for date utility class.
 */
public class DateUtilTest {
  private int day1;
  private int day2;

  /**
   * Set up for DateUtil test.
   */
  @Before
  public void setUp() {
    day1 = 20170701;
    day2 = 20170523;
  }

  /**
   * Tests if the method correctly returns the difference (in days) between two dates.
   */
  @Test
  public void timeDiffTest() throws Exception {
    assertEquals(39, DateUtil.timeDiff(day1, day2));
    assertEquals(8, DateUtil.timeDiff(day1, 20170623));

  }

  /**
   * Tests if the minus days method returns the correct new LocalDay object.
   */
  @Test
  public void minusDayTest() throws Exception {
    LocalDate result = DateUtil.minusDay(3, 1, 7, 2017);
    assertEquals(28, result.getDayOfMonth());
    assertEquals(6, result.getMonthValue());
    assertEquals(2017, result.getYear());
  }

  @Test
  public void convertInt() {
    LocalDate test = LocalDate.of(2017, 1, 1);
    assertEquals(20170101, DateUtil.convertInt(test));
  }


}
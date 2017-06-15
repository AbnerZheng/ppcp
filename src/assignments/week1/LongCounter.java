package assignments.week1;

/**
 * Created by abnerzheng on 2017/6/14.
 */
public class LongCounter {
  private long count = 0;
  public synchronized void increment() {
    count = count + 1;
  }
  public synchronized long get() {
    return count;
  }
}

package assignments.week2;// For week 2
// sestoft@itu.dk * 2014-09-04

import javax.annotation.concurrent.ThreadSafe;

class SimpleHistogram {
  public static void main(String[] args) throws InterruptedException {
    final Histogram histogram = new Histogram2(30);

      Thread thread = new Thread(()->{
          for (int i = 0; i < 100000; i++) {
              histogram.increment(10);
          }
      });
      Thread thread1 = new Thread(() -> {
          for (int i = 0; i < 100000; i++) {
              histogram.increment(10);
          }
      });
      thread.start();
      thread1.start();

      thread.join();
      thread1.join();
      dump(histogram);
  }

  public static void dump(Histogram histogram) {
    int totalCount = 0;
    for (int bin=0; bin<histogram.getSpan(); bin++) {
      System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
      totalCount += histogram.getCount(bin);
    }
    System.out.printf("      %9d%n", totalCount);
  }
}

interface Histogram {
  public void increment(int bin);
  public int getCount(int bin);
  public int getSpan();
}

class Histogram1 implements Histogram {
  private int[] counts;
  public Histogram1(int span) {
    this.counts = new int[span];
  }
  public void increment(int bin) {
    counts[bin] = counts[bin] + 1;
  }
  public int getCount(int bin) {
    return counts[bin];
  }
  public int getSpan() {
    return counts.length;
  }
}

@ThreadSafe
class Histogram2 implements Histogram{
  private final int[] counts;

  Histogram2(int span) {
    this.counts = new int[span];
  }

  @Override
  public synchronized void increment(int bin) {
    counts[bin]++;
  }

  @Override
  public synchronized int getCount(int bin) {
    return counts[bin];
  }

  @Override
  public int getSpan() {
    return counts.length;
  }
}

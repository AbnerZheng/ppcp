package assignments.week4;// Week 4
// Counting primes, using multiple threads for better performance.
// (Much simplified from CountprimesMany.java)
// sestoft@itu.dk * 2014-08-31, 2015-09-15

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntToDoubleFunction;

import static assignments.benchmark.benchmark.Mark7;
import static assignments.benchmark.benchmark.SystemInfo;

public class TestCountPrimesThreads {
  public static void main(String[] args) {
    SystemInfo();
    final int range = 100_000;
    // Mark6("countSequential", i -> countSequential(range));
    // Mark6("countParallel", i -> countParallelN(range, 10));
    Mark7("countSequential", i -> countSequential(range));
    for (int c=1; c<=16; c++) {
      final int threadCount = c;
      Mark7(String.format("countParallelLocal %6d", threadCount), 
            i -> countParallelNLocal(range, threadCount));
    }
  }

  private static boolean isPrime(int n) {
    int k = 2;
    while (k * k <= n && n % k != 0)
      k++;
    return n >= 2 && k * k > n;
  }

  // Sequential solution
  private static long countSequential(int range) {
    long count = 0;
    final int from = 0, to = range;
    for (int i=from; i<to; i++)
      if (isPrime(i)) 
        count++;
    return count;
  }

  // General parallel solution, using multiple threads
  private static long countParallelN(int range, int threadCount) {
    final int perThread = range / threadCount;
//    final LongCounter lc = new LongCounter();
    final AtomicLong lc = new AtomicLong();
    Thread[] threads = new Thread[threadCount];
    for (int t=0; t<threadCount; t++) {
      final int from = perThread * t, 
        to = (t+1==threadCount) ? range : perThread * (t+1); 
      threads[t] = new Thread(new Runnable() { public void run() {
        long count = 0;
        for (int i=from; i<to; i++)
          if (isPrime(i))
              count ++;
        lc.addAndGet(count);
      }});
    }
    for (int t=0; t<threadCount; t++) 
      threads[t].start();
    try {
      for (int t=0; t<threadCount; t++) 
        threads[t].join();
    } catch (InterruptedException exn) { }
    return lc.get();
  }

  // General parallel solution, using multiple threads
  private static long countParallelNLocal(int range, int threadCount) {
    final int perThread = range / threadCount;
    final long[] results = new long[threadCount];
    Thread[] threads = new Thread[threadCount];
    for (int t=0; t<threadCount; t++) {
      final int from = perThread * t, 
        to = (t+1==threadCount) ? range : perThread * (t+1); 
      final int threadNo = t;
      threads[t] = new Thread(new Runnable() { public void run() {
        long count = 0;
        for (int i=from; i<to; i++)
          if (isPrime(i))
            count++;
        results[threadNo] = count;
      }});
    }
    for (int t=0; t<threadCount; t++) 
      threads[t].start();
    try {
      for (int t=0; t<threadCount; t++) 
        threads[t].join();
    } catch (InterruptedException exn) { }
    long result = 0;
    for (int t=0; t<threadCount; t++) 
      result += results[t];
    return result;
  }
}

class LongCounter {
  private long count = 0;
  public synchronized void increment() {
    count = count + 1;
  }
  public synchronized long get() { 
    return count; 
  }
}

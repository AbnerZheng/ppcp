package assignments.week2;

import java.util.concurrent.atomic.AtomicInteger;

public class TestHistogram {
    /**
     Week 2
     @Authors: Cem Turan, Klaus Klokmose Nielsen

     Exercise 2.1
     1. Yes we observe the same; that mi.value remains invisible and the thread keeps running
     2. Yes, the thread terminates as expected
     3. The thread does not terminate. Both methods need to be synchronized in order to provide visibility.
     4. Yes it terminates as expected. It should be sufficient to use volatile field modifier because it provides visibility (but not mutual exclusion, which is not needed in this example)

     Exercise 2.2 - TestCountPrimes
     1. run the timer: Result: 7,9 sec
     2. It is faster? Running time; 1,75 sec
     3. Not same result: we get 663744
     4. It does not matter. The get method is only called after the .join() on each of the examples, thus only one thread is calling the method (namely the Main-thread)

     Exercise 2.3
     1. Compute the total number of prime factors in the range 0 - 4,999,999:
     (Should be 18,703,729)
     Is correct: 18,703,729, this took 8,016 seconds
     2. class is written in the java file (exercise_2_3.java)
     3. Program written in the java file (exercise_2_3.java) The execution time: 1,732 seconds
     4. (Yes) the volatile modifier can be used in the previous example, but only because the 10 threads don't write to the longCounter field at the same time,
     thus this is not recommended as we don't know when the threads are finished.
     5. Using AtomicInteger we get the same result and the execution time: 1,744 seconds
     AtomicInteger is faster than a lock, but we get the same result and in approx. the same time. The same argument from question 4 also holds here, i.e. 10 threads don't write to the field at the same time.

     Exercise 2.4
     1. It is important because if the cache field is not declared volatile there would be no visibility (lost updates).
     2. It is important because the object needs to be immutable. When the object is immutable, initialization safety is obtained, such that any thread can use the cache in a consistent state.

     */
    public static void main(String[] args) {
        /***
         * 1.
         */
        final int range = 5_000_000;
        int count = sequentialPSum(0, range);
        System.out.printf("Total number of factors is %9d%n", count);

        /***
         * 2.3.3
         */
        MyAtomicInteger2 counter = new MyAtomicInteger2();
        System.out.println(parallelPSum(range, 10, counter));

        /***
         * 2.3.5
         */
        AtomicIntegerClone counter2 = new AtomicIntegerClone();
        System.out.println(parallelPSum(range, 10, counter2));
    }

    public static int sequentialPSum(int from, int to) {
        int count = 0;
        for (;from < to; from++)
            count += countFactors(from);
        return count;
    }

    public static long parallelPSum(int range, int threadCount, final Counter counter){
		/* final (not necessary)*/
        Thread[] threads = new Thread[threadCount];

        final int rangePerThread = (int) Math.floor(range / threadCount);


        for (int i = 0; i < threadCount; i++) {
            final int i2 = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    int from = i2 * rangePerThread;
                    int to = (i2+1) * rangePerThread;
                    counter.addAndGet((sequentialPSum(from, to)));
                }
            });

        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {}
        }

        return counter.get();
    }

    public static int countFactors(int p) {
        if (p < 2)
            return 0;
        int factorCount = 1, k = 2;
        while (p >= k * k) {
            if (p % k == 0) {
                factorCount++;
                p /= k;
            } else
                k++;
        }
        return factorCount;
    }
}

interface Counter {
    int addAndGet(int amount);

    int get();
}

class AtomicIntegerClone extends AtomicInteger implements Counter{}


class MyAtomicInteger2 implements Counter{
    private int i;

    public synchronized int addAndGet(int amount){
        i += amount;
        return i;
    }

    public synchronized int get(){
        return i;
    }
}
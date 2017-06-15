package assignments.week2;
// For week 2
// sestoft@itu.dk * 2014-08-29

import java.util.concurrent.atomic.AtomicInteger;

class MyAtomicInteger{
    private int count = 0;
    public synchronized int addAndGet(int amount){
        count += amount;
        return count;
    }
    public synchronized int get(){
        return count;
    }
}

class TestCountFactors {

    public static final int RANGE = 5_000_000;
    public static final int NTHREAD= 10;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int count = 0;
//        MyAtomicInteger myAtomicInteger = new MyAtomicInteger();
        final AtomicInteger atomicInteger = new AtomicInteger();
        Thread[] threads = new Thread[NTHREAD];
        for (int i = 0; i < NTHREAD; i++){
            final int ii = i;
            threads[i] = new Thread(()->{
                atomicInteger.addAndGet(addPart(ii));
            });
            threads[i].start();
        }

        for (int i = 0; i < NTHREAD; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        System.out.printf("Total number of factors is %9d%n", atomicInteger.get());
        System.out.printf("Total time %d\n",end - start); // sequential: 7396
    }

    public static int addPart(int index){
        int start = RANGE/NTHREAD * index;
        int end = start + RANGE/NTHREAD;
        int count = 0;
        while(start< end){
            count += countFactors(start++);
        }
        return count;
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

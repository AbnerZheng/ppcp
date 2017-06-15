package lecture1;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by abnerzheng on 2017/6/14.
 */
class LongCounterLecture {
    protected long count = 0;

    public void increase(){
        count ++;
    }
    public synchronized long getCounter(){
        return count;
    }
}

class LongCountSynchLecture extends LongCounterLecture {
    public LongCountSynchLecture(){
        super();
    }

    @Override
    public synchronized void increase() {
        count++;
    }
}

class LongCountSyncOnObLecture extends LongCounterLecture {
    private final Object myLock;
    public LongCountSyncOnObLecture(){
        super();
        myLock = new Object();
    }
    public void increase(){
        synchronized (myLock){
            count ++;
        }
    }
}

class LongCounterLectureWrong extends LongCounterLecture {
    @Override
    public void increase() {
        count++;
    }
}

class LongCounterLectureAtomic extends LongCounterLecture {
    private AtomicLong atomicLong;

    public LongCounterLectureAtomic(){
        atomicLong = new AtomicLong();
    }

    @Override
    public void increase() {
        atomicLong.addAndGet(1);
    }

    @Override
    public synchronized long getCounter() {
        return atomicLong.get();
    }
}

public class TestLongCounter{

    public static final int INT = 100000;

    public static void main(String[] args) throws IOException {
        final LongCountSynchLecture longCounter = new LongCountSynchLecture();
        compare(longCounter);

        final LongCountSyncOnObLecture longCountSyncOnOb = new LongCountSyncOnObLecture();
        compare(longCountSyncOnOb);

        final LongCounterLectureWrong longCounterWrong = new LongCounterLectureWrong();
        compare(longCounterWrong);

        final LongCounterLectureAtomic longCounterAtomic = new LongCounterLectureAtomic();
        compare(longCounterAtomic);
    }

    private static void compare(LongCounterLecture longCounter) {
        long startTime = System.currentTimeMillis();
        Thread t1 = new Thread(()->{
            int i = 0;
            while (i++ < INT){
                longCounter.increase();
            }
        });

        Thread t2 = new Thread(()->{
            int i = 0;
            while(i++ < INT){
                longCounter.increase();
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
        System.out.println(longCounter.getCounter() == 2* INT);
    }
}

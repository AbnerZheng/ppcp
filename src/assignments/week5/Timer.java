package assignments.week5;

/**
 * Created by abnerzheng on 2017/6/19.
 */

public class Timer {
    private long start, spent = 0;

    public Timer() {
        play();
    }

    public double check() {
        return (System.nanoTime() - start + spent) / 1e9;
    }

    public void pause() {
        spent += System.nanoTime() - start;
    }

    public void play() {
        start = System.nanoTime();
    }
}

package lecture1;

import java.io.IOException;

/**
 * Created by abnerzheng on 2017/6/14.
 *
 */

class MutableInteger {
    private int value = 0;
    public void set(int value) {
        this.value = value;
    }
    public int get() {
        return value;
    }
}

public class TestVisibility {
    public static void main(String[] args) throws IOException {
        final MutableInteger mutableInteger = new MutableInteger();
        Thread t1 = new Thread(()->{
            while (mutableInteger.get() == 0){
            }
            System.out.println("I completed, mi = " + mutableInteger.get());
        });

        t1.start();
        System.out.println("Press Enter to set mi to 42:");
        System.in.read();                   // Wait for enter key
        mutableInteger.set(42);
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("success");
    }
}

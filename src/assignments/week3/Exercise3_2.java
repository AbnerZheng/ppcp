package assignments.week3;

import sun.jvm.hotspot.utilities.Assert;

import javax.lang.model.type.ArrayType;
import java.util.Arrays;

/**
 * Created by abnerzheng on 2017/6/16.
 */
public class Exercise3_2 {

    final static int N = 10_000_001;

    public static void main(String[] args) {
        int[] a = new int[N];
        Arrays.parallelSetAll(a, index->isPrime(index)?1:0);
        for (int i = 0; i < 10; i++) {
            System.out.println(a[i]);
        }
        Arrays.parallelPrefix(a, (pre, cur)-> pre + cur);
        System.out.println(a[N-1]);
        assert a[N-1] == 664_579;

        for (int i = 0; i < 10; i++) {
            int i1 = N / 10 * i + 1;
            System.out.println(a[i1] * Math.log(i1)/ i1);
        }
    }
    private static boolean isPrime(int n) {
        int k = 2;
        while (k * k <= n && n % k != 0)
            k++;
        return n >= 2 && k * k > n;
    }
}

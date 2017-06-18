package assignments.week3;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Created by abnerzheng on 2017/6/18.
 */
public class Exercise3_4 {
    public static final int N = 999_999_999 + 1;
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        DoubleStream doubleStream = IntStream.range(1, N).mapToDouble(x -> 1.0 / x);
//        double sum = doubleStream.sum();
//        final Double[] sum = {0.0};
//        doubleStream.forEach(x-> sum[0] += x);


        double sum = DoubleStream.iterate(1, x -> x + 1).limit(N).parallel().map(x -> 1.0 / x).sum();
        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.printf("Sum = %20.16f, time= %d \n", sum, end - start);
    }
}

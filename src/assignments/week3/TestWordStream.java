package assignments.week3;// Week 3
// sestoft@itu.dk * 2015-09-09

import javax.lang.model.type.ArrayType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestWordStream {
  public static void main(String[] args) {
    String filename = "/usr/share/dict/words";
    Stream<String> stringStream = readWords(filename);
    /**
     * start of Exercise3.3.1
     */
//    System.out.println(stringStream.count() == 235_886);
    /**
     * end of Exercise3.3.1
     */

    /**
     * start of exercise3.3.2
     */
//    stringStream.limit(100).forEach(System.out::println);
    /**
     * end of exercise3.3.2
     */
    /**
     * start of exercise3.3.3
     */
//    Stream<String> stringStream1 = stringStream.parallel().filter(x -> x.length() >= 22);
//    stringStream1.forEach(System.out::println);
    /**
     * end of exercise3.3.3
     */
    /**
     * start of exercise3.3.4
     */
//    String first = stringStream.parallel().filter(x -> x.length() >= 22).findFirst().orElse("");
//    System.out.println(first);
    /**
     * end of exercise3.3.4
     */

    /**
     * start of exercise3.3.5
     */
//    long start = System.currentTimeMillis();
//    Stream<String> stringStream3 = stringStream.filter(TestWordStream::isPalindrome);
//    stringStream3.forEach(System.out::println);
//    long end = System.currentTimeMillis();
//    System.out.printf("before: %d", end - start);
    /**
     * end of exercise3.3.5
     */
    String[] strings = stringStream.toArray(String[]::new);
    Stream<String> stream5 = Arrays.stream(strings);

    Stream<Integer> integerStream = stream5.map(String::length);
    Integer[] objects = integerStream.toArray(Integer[]::new);
    Stream<Integer> stream3 = Arrays.stream(objects);
    Integer max = stream3.max(Comparator.comparingInt(a -> a)).orElse(0);
    Stream<Integer> stream = Arrays.stream(objects);
    Integer min = stream.min(Comparator.comparingInt(a -> a)).orElse(0);
    Stream<Integer> stream1 = Arrays.stream(objects);
    Integer sum= stream1.reduce((pre, cur) -> pre + cur).orElse(0);
    Stream<Integer> stream2 = Arrays.stream(objects);
    double avg = 1.0 * sum / stream2.count();
    System.out.printf("max: %d, min: %d, avg: %f", max, min, avg);


    Stream<String> stream4 = Arrays.stream(strings);
    Map<Object, List<String>> collect = stream4.collect(Collectors.groupingBy(x ->x.length()));
    collect.forEach((x, xs)->{
      System.out.printf("%d: \n", x);
      System.out.println(xs);
    });


    Stream<String> stream6 = Arrays.stream(strings);
    ConcurrentHashMap<Character, Integer> characterIntegerConcurrentHashMap = new ConcurrentHashMap<>();
    for (char c = 'a'; c <= 'z'; c++) {
        characterIntegerConcurrentHashMap.put(c, 0);
    }
    stream6.parallel().forEach(x->{
      String s = x.toLowerCase();
      for (int i = 0; i < s.length(); i++) {
        characterIntegerConcurrentHashMap.computeIfPresent(s.charAt(i), (k,v)->v+1);
      }
    });

    System.out.println(letters("persistent"));
    Stream<String> stream7 = Arrays.stream(strings);
    Map<Map<Character, Integer>, List<String>> collect1 = stream7.collect(Collectors.groupingBy(x -> letters(x)));
    System.out.println(collect1.size());
  }

  public static Stream<String> readWords(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      // TO DO: Implement properly
      return reader.lines();
    } catch (IOException exn) {
      return Stream.<String>empty();
    }
  }

  public static boolean isPalindrome(String s) {
    return new StringBuilder(s).reverse().toString().equals(s);
  }

  public static Map<Character,Integer> letters(String s) {
    Map<Character,Integer> res = new TreeMap<>();
    // TO DO: Implement properly
    s.chars().forEach(c -> {
          res.compute((char) c,
              (key, value) -> {
                if (value == null) {
                  return 1;
                } else {
                  return value+1;
                }
              });
        }
    );
    return res;
  }
}

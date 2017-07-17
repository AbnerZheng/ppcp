package assignments.week5;// For week 5
// sestoft@itu.dk * 2014-09-19

import javafx.util.Pair;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class TestDownload {
    private static final ExecutorService executor = Executors.newWorkStealingPool();
    private static final String[] urls =
        {"http://www.itu.dk", "http://www.di.ku.dk",
            "http://www.microsoft.com", "http://www.amazon.com", "http://www.dr.dk",
            "http://www.vg.no", "http://www.tv2.dk", "http://www.google.com",
            "http://www.ing.dk", "http://www.dtu.dk", "http://www.eb.dk",
            "http://www.guardian.co.uk", "http://www.welt.de", "http://www.dn.se", "http://www.heise.de",
            "http://www.bbc.co.uk", "http://www.dsb.dk", "http://www.bmw.com", "https://www.cia.gov"
        };

    public static void main(String[] args) throws IOException {
        getPagesParallel(urls, 200);
    }

    public static String getPage(String url, int maxLines) throws IOException {
        // This will close the streams after use (JLS 8 para 14.20.3):
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < maxLines; i++) {
                String inputLine = in.readLine();
                if (inputLine == null)
                    break;
                else
                    sb.append(inputLine).append("\n");
            }
            return sb.toString();
        }
    }

    public static Map<String, String>  getPages(String[] urls, int maxLines) throws IOException{
        Map<String, String> result = new HashMap<>();
        for (String url : urls) {
            System.out.println(url);
            result.put(url,getPage(url, maxLines));
        }
        return result;
    }

    public static Map<String, String> getPagesParallel(String[] urls, int maxLines){
        List<Callable<Pair<String, String>>> tasks = new ArrayList<>();
        Map<String, String> result = new HashMap<>();
        for (String url : urls) {
            tasks.add(
                ()-> new Pair<>(url, getPage(url, maxLines))
            );
        }
        try {
            List<Future<Pair<String, String>>> futures = executor.invokeAll(tasks);
            for (Future<Pair<String, String>> future:futures){
                Pair<String, String> stringStringPair = future.get();
                result.put(future.get().getKey(), stringStringPair.getValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }


}


package Crawler;

import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Queue;

public class CrawlThread implements Runnable {
    HashMap<String, Integer> urlCount;
    Queue<URLTrial> urlQueue;
    URLTrial targetURL;

    Printer printer;
    final int toleration;
    final int maxDepth;

    public CrawlThread(HashMap<String, Integer> urlCount,
                       Queue<URLTrial> urlQueue, URLTrial targetURL, Printer printer) {
        this.urlCount = urlCount;
        this.urlQueue = urlQueue;
        this.targetURL = targetURL;
        this.printer = printer;
        toleration = 10;
        maxDepth = 5;
    }

    public CrawlThread(HashMap<String, Integer> urlCount,
                       Queue<URLTrial> urlQueue, URLTrial targetURL, Printer printer,
                       int toleration, int maxDepth) {
        this.urlCount = urlCount;
        this.urlQueue = urlQueue;
        this.targetURL = targetURL;
        this.printer = printer;
        this.toleration = toleration;
        this.maxDepth = maxDepth;
    }

    @Override
    public void run() {
        try {
            Document doc = Jsoup.connect(targetURL.url).get();
            String html = doc.toString();
            String text = Jsoup.parse(html).text();
            printer.print(targetURL.url, text);

            if(targetURL.depth>=maxDepth) {
                return;
            }

            Elements links = doc.select("a[href]");

            for(Element e : links) {
                String url = e.attr("abs:href");
                url = url.split("#")[0];
                if(URLTrial.isValid(url) && getHostName(targetURL.url).equals(getHostName(url))) {
                    if(url.equals(targetURL.url)) {
                        continue;
                    }
                    if(urlCount.containsKey(url) ) {
                        synchronized (urlCount) {
                            urlCount.put(url, urlCount.get(url) + 1);
                        }
                    } else {
                        synchronized (urlCount) {
                            urlCount.put(url, 1);
                        }

                        synchronized (urlQueue) {
                            urlQueue.add(new URLTrial(url, targetURL.depth + 1));
                        }
                    }
                }
            }
        } catch (IOException | UncheckedIOException e) {
            System.out.println("Failure to connect: " + targetURL.url);
            targetURL.failCount++;
            if(targetURL.failCount<toleration) urlQueue.add(targetURL);
            //e.printStackTrace();
        }
    }

    private static String getHostName(String url) throws MalformedURLException {
        return new URL(url).getHost();
    }
}

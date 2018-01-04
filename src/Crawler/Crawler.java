package Crawler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Crawler {
    HashMap<String, String> urlCacheHash;
    HashMap<String, Integer> urlCount;
    Queue<URLTrial> urlQueue;
    String[] rootURLs;

    public Crawler(String[] rootURLs) {
        urlCacheHash = new HashMap<String, String>();
        urlCount = new HashMap<String, Integer>();
        urlQueue = new LinkedList<URLTrial>();
        this.rootURLs = rootURLs;
    }

    void init() {
        for(String s : rootURLs) {
            urlQueue.add(new URLTrial(s));
            urlCount.put(s,1);
        }
    }

    public void crawl(int maxThread) throws InterruptedException {
        init();

        ExecutorService threadPool = Executors.newFixedThreadPool(maxThread);

        while (true){
            System.out.println("ekhon ache: " + urlQueue.size());
            if (urlQueue.isEmpty()) {
                //if(threadPool.ge)
                Thread.sleep(1000);
                continue;
            }
            URLTrial top = urlQueue.remove();
            System.out.println(top.url);
            threadPool.execute(new CrawlThread(urlCacheHash, urlCount, urlQueue, top, "./" ));
        }

        //threadPool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        String[] root = {"http://www.feynman.com/"};
        Crawler spiderMan = new Crawler(root);
        spiderMan.crawl();
    }

    public void crawl() throws InterruptedException {
        crawl(50);
    }
}

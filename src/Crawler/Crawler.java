package Crawler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Crawler {
    HashMap<String, String> urlCacheHash;
    HashMap<String, Integer> urlCount;
    Queue<URLTrial> urlQueue;
    String[] rootURLs;

    public Crawler(String[] rootURLs) {
        urlCacheHash = new HashMap<>();
        urlCount = new HashMap<>();
        urlQueue = new LinkedList<>();
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

        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThread);
        System.out.println("Alive: " +threadPool.getKeepAliveTime( TimeUnit.MILLISECONDS) + "");
        while (true){
            if (urlQueue.isEmpty()) {
                if (threadPool.getActiveCount()==0)   break;
                Thread.sleep(1000);
                continue;
            }

            URLTrial top = urlQueue.remove();
            System.out.println(top.url + " " + top.failCount + " " + top.depth);
            threadPool.execute(new CrawlThread(urlCacheHash, urlCount, urlQueue, top, "./" ));
        }

        threadPool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        String[] root = {"http://www.svvs.org/"};
        Crawler spiderMan = new Crawler(root);
        spiderMan.crawl();
    }

    public void crawl() throws InterruptedException {
        crawl(10);
    }
}

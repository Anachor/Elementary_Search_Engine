package CrawlerForWikipedia;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Crawler {
    HashMap<String, Integer> urlCount;
    Queue<URLTrial> urlQueue;
    String[] rootURLs;
    Printer printer;
    String targetDirectory;

    public Crawler(String[] rootURLs, String targetDirectory) {
        urlCount = new HashMap<>();
        urlQueue = new LinkedList<>();
        this.rootURLs = rootURLs;
        this.targetDirectory = targetDirectory;
        printer = new Printer(targetDirectory);
    }

    private void init() {
        for(String s : rootURLs) {
            urlQueue.add(new URLTrial(s));
            urlCount.put(s,1);
        }
    }

    public void crawl(int maxThread) throws InterruptedException {
        init();

        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThread);
        while (true){
            if (urlQueue.isEmpty()) {
                if (threadPool.getActiveCount()==0)   break;
                Thread.sleep(1000);
                continue;
            }

            URLTrial top = urlQueue.remove();
            System.out.println(top.url + " " + top.failCount + " " + top.depth + " "+urlQueue.size());
            threadPool.execute(new CrawlThread(urlCount, urlQueue, top, printer ));
        }

        threadPool.shutdown();
        threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        printer.close();
    }

    public static void main(String[] args) throws InterruptedException {
        String[] root = {"https://en.wikipedia.org/wiki/The_Meaning_of_It_All",
                "https://en.wikipedia.org/wiki/List_of_algorithms",
                "https://en.wikipedia.org/wiki/Bangladesh"};

        Crawler spiderMan = new Crawler(root, "./Crawldata1");
        spiderMan.crawl();
    }

    public void crawl() throws InterruptedException {
        try {
            crawl(10);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println(urlQueue.size());
        }
    }
}

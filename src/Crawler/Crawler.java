package Crawler;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Crawler {
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

            System.out.print(urlQueue.size() + " ");
            System.out.print(urlQueue.peek() + " ");
            System.out.print(urlQueue.size() + " ");

            URLTrial top = urlQueue.remove();
            System.out.println(top.url + " " + top.failCount + " " + top.depth + " "+urlQueue.size());
            threadPool.execute(new CrawlThread(urlCount, urlQueue, top, printer ));
        }

        threadPool.shutdown();
        printer.close();
    }

    public static void main(String[] args) throws InterruptedException {
        String[] root = {"https://en.wikipedia.org/wiki/Main_Page"};
        Crawler spiderMan = new Crawler(root, "./Crawldata");
        spiderMan.crawl();
    }

    public void crawl() throws InterruptedException {
        crawl(10);
    }
}

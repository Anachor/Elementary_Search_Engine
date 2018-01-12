package Crawler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Printer {
    String targetDirectory;
    HashMap<String, String> urlCacheHash;
    ExecutorService threadPool;

    public Printer(String targetDirectory) {
        this.targetDirectory = targetDirectory;
        this.urlCacheHash = new HashMap<>();
        threadPool = Executors.newFixedThreadPool(10);
    }

    public void print(String url, String text) {
        String fileName = targetDirectory + "/" + java.util.UUID.randomUUID().toString();
        urlCacheHash.put(url, fileName);
        
        threadPool.execute(new PrinterThread(fileName, text, url));
    }

    public void close() {
        threadPool.shutdown();
    }

}

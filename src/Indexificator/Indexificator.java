package Indexificator;


import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Indexificator {
    HashMap<String,File> documentMonitor;
    ExecutorService executorService;

    public Indexificator() {
        documentMonitor =  new HashMap<>();
        executorService = Executors.newFixedThreadPool(50);
    }

    public Indexificator(int maxThread) {
        documentMonitor = new HashMap<>();
        executorService = Executors.newFixedThreadPool(maxThread);
    }



    private void buildIndex(final String sourceDir,final String targetDir) {
        File source = new File(sourceDir);
        FilenameFilter crawldataFilter = (dir, name)->name.toLowerCase().endsWith(".crawldata");

        for (String crawlFile : source.list(crawldataFilter)) {
            executorService.execute(new IndexerThread(sourceDir,targetDir,crawlFile,documentMonitor));
        }

        executorService.shutdown();
    }
/*

    public void flushIndex(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(invertedIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
/*
    public void flushDocumentFrequency (String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(documentFrequency);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


/*
    public void index(String sourceDir,String targetDir) throws InterruptedException {
        File source = new File(sourceDir);
        FilenameFilter crawldataFilter = (dir, name)->name.toLowerCase().endsWith(".crawldata");

        for (String file : source.list(crawldataFilter)) {
            executorService.execute(new IndexerThread(sourceDir,file,invertedIndex));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);

        for(String key : invertedIndex.keySet()) {
            invertedIndex.get(key).sort((x,y)->y.score-x.score);
        }

        Thread t1 = new Thread(()->flushIndex(targetDir + "/" + "invertedIndex.idx"));
        t1.start();

        t1.join();

    }
*/

    public static void main(String[] args) throws InterruptedException {

        new Indexificator(10).buildIndex("Crawldata","InvertedIndex");
    }

}

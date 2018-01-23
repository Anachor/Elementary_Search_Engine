package Indexificator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Indexificator {
    HashMap<String,ArrayList<URLTermFrequencyPair>> invertedIndex;
    //HashMap<String,Integer> documentFrequency;
    ExecutorService executorService;

    public Indexificator(HashMap<String, ArrayList<URLTermFrequencyPair> > invertedIndex) {
        this.invertedIndex = invertedIndex;
        //this.documentFrequency = documentFrequency;
        executorService = Executors.newFixedThreadPool(50);
    }

    public Indexificator(HashMap<String, ArrayList<URLTermFrequencyPair> > invertedIndex, int maxThread) {
        this.invertedIndex = invertedIndex;
        //this.documentFrequency = documentFrequency;
        executorService = Executors.newFixedThreadPool(maxThread);
    }

    public Indexificator() {
        invertedIndex = new HashMap<>();
        //documentFrequency = new HashMap<>();
        executorService = Executors.newFixedThreadPool(50);
    }

    public Indexificator(int maxThread) {
        invertedIndex = new HashMap<>();
        //documentFrequency = new HashMap<>();
        executorService = Executors.newFixedThreadPool(maxThread);
    }



    private void buildIndex(final String sourceDir) {
        File source = new File(sourceDir);
        FilenameFilter crawldataFilter = (dir, name)->name.toLowerCase().endsWith(".crawldata");

        for (String file : source.list(crawldataFilter)) {
            executorService.execute(new IndexerThread(sourceDir,file,invertedIndex));
        }

        executorService.shutdown();
    }

    public void flushIndex(String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(invertedIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*
    public void flushDocumentFrequency (String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(documentFrequency);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    public void index(String sourceDir,String targetDir) throws InterruptedException {
        File source = new File(sourceDir);
        FilenameFilter crawldataFilter = (dir, name)->name.toLowerCase().endsWith(".crawldata");

        for (String file : source.list(crawldataFilter)) {
            executorService.execute(new IndexerThread(sourceDir,file,invertedIndex));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);



        Thread t1 = new Thread(()->flushIndex(targetDir + "/" + "invertedIndex.idx"));
        t1.start();

        t1.join();

    }

    public static void main(String[] args) throws InterruptedException {

        new Indexificator(10).index("Crawldata1","TargetF");
    }

}

package Indexificator;

import Indexer.ForwardIndexMaker;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Indexificator {
    HashMap<String,ArrayList<ScoredURL>> invertedIndex;
    HashMap<String,Integer> documentFrequency;
    ExecutorService executorService;

    public Indexificator(HashMap<String, ArrayList<ScoredURL> > invertedIndex,
                         HashMap<String, Integer> documentFrequency) {
        this.invertedIndex = invertedIndex;
        this.documentFrequency = documentFrequency;
        executorService = Executors.newFixedThreadPool(50);
    }

    public Indexificator(HashMap<String, ArrayList<ScoredURL> > invertedIndex,
                         HashMap<String, Integer> documentFrequency, int maxThread) {
        this.invertedIndex = invertedIndex;
        this.documentFrequency = documentFrequency;
        executorService = Executors.newFixedThreadPool(maxThread);
    }

    public Indexificator() {
        invertedIndex = new HashMap<>();
        documentFrequency = new HashMap<>();
        executorService = Executors.newFixedThreadPool(50);
    }

    public Indexificator(int maxThread) {
        invertedIndex = new HashMap<>();
        documentFrequency = new HashMap<>();
        executorService = Executors.newFixedThreadPool(maxThread);
    }



    private void buildIndex(final String sourceDir) {
        File source = new File(sourceDir);
        FilenameFilter crawldataFilter = (dir, name)->name.toLowerCase().endsWith(".crawldata");

        for (String file : source.list(crawldataFilter)) {
            executorService.execute(new IndexerThread(sourceDir,file,invertedIndex,documentFrequency));
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

    public void flushDocumentFrequency (String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(documentFrequency);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void index(String sourceDir,String targetDir) throws InterruptedException {
        File source = new File(sourceDir);
        FilenameFilter crawldataFilter = (dir, name)->name.toLowerCase().endsWith(".crawldata");

        for (String file : source.list(crawldataFilter)) {
            executorService.execute(new IndexerThread(sourceDir,file,invertedIndex,documentFrequency));
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);

        for(String key : invertedIndex.keySet()) {
            invertedIndex.get(key).sort((x,y)->y.score-x.score);
        }

        Thread t1 = new Thread(()->flushIndex(targetDir + "/" + "invertedIndex.idx"));
        t1.start();
        Thread t2 = new Thread(()->flushDocumentFrequency(targetDir + "/" + "documentFrequency.freq"));
        t2.start();

        t1.join();
        t2.join();

    }

    public static void main(String[] args) throws InterruptedException {

        new Indexificator(10).index("Crawldata","InvertedIndex");
    }

}

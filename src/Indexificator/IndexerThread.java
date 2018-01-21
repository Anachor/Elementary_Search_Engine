package Indexificator;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import java.io.*;
import java.util.HashMap;
import java.util.List;

public class IndexerThread implements Runnable {
    final String sourceDir;
    final String targetDir;
    final String crawlFile;
    HashMap<String,Integer> forwardIndex;
    HashMap<String,File> documentMonitor;

    public IndexerThread(String sourceDir, String targetDir, String crawlFile,
                         HashMap<String, File> documentMonitor) {
        this.sourceDir = sourceDir;
        this.targetDir = targetDir;
        this.crawlFile = crawlFile;
        this.forwardIndex = new HashMap<>();
        this.documentMonitor = documentMonitor;
    }



    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceDir+"/"+crawlFile))) {
            String url = bufferedReader.readLine();

            String str;
            String doc = "";
            while ((str = bufferedReader.readLine()) != null) {
                doc = doc + str;
            }

            for (Sentence sent :new Document(doc).sentences()) {
                String s = sent.toString();
                s = s.toLowerCase();
                s = s.replaceAll("[\\.,!?;'~/\\-()\\[\\]{}:`\"]","");

                try {
                    Sentence sentence = new Sentence(s);

                    List<String> lemmas = sentence.lemmas();

                    for (String l : lemmas) {
                        if (forwardIndex.containsKey(l)) {
                            forwardIndex.put(l, forwardIndex.get(l) + 1);
                        } else {
                            forwardIndex.put(l, 1);
                        }
                    }
                } catch (IllegalStateException e) {
                    System.out.println("Empty Line");
                }

            }


            for(String word : forwardIndex.keySet()) {
                synchronized (documentMonitor) {
                    if(!documentMonitor.containsKey(word)) {
                        documentMonitor.put(word,new File(targetDir + "/" + word + ".idx"));
                    }
                }

                synchronized (documentMonitor.get(word)) {
                    //System.out.println(documentMonitor.get(word).getPath());

                    try (FileWriter writer = new FileWriter(documentMonitor.get(word).getPath(),true)) {
                        writer.write(url + " " + forwardIndex.get(word) + "\n");
                    } catch (FileNotFoundException e) {
                        System.out.println("Could not write to file");
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("IO exception");
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println("Hello Darkness my old friend :)");
                        e.printStackTrace();
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("CrawlFile not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception at crawlfile");
            e.printStackTrace();
        }
    }

}

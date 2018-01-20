package Indexificator;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexerThread implements Runnable {
    final String sourceDir;
    final String crawlFile;
    HashMap<String,Integer> forwardIndex;
    HashMap<String,ArrayList<ScoredURL> > invertedIndex;
    //HashMap<String,Integer> documentFrequency;

    public IndexerThread(String sourceDir, String crawlFile,
                         HashMap<String, ArrayList<ScoredURL>> invertedIndex) {

        this.sourceDir = sourceDir;
        this.crawlFile = crawlFile;
        this.forwardIndex = new HashMap<>();
        this.invertedIndex = invertedIndex;
        //this.documentFrequency = documentFrequency;
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
                if(!invertedIndex.containsKey(word)) {
                    synchronized (invertedIndex) {
                        invertedIndex.put(word,new ArrayList<>());
                    }
                }

                synchronized (invertedIndex) {
                    invertedIndex.get(word).add(new ScoredURL(url, forwardIndex.get(word)));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

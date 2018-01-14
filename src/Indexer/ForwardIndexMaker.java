package Indexer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import edu.stanford.nlp.simple.*;


public class ForwardIndexMaker implements Runnable {
    final String targetDir;
    final String sourceDir;
    final String crawlFile;
    HashMap<String,Integer> forwardIndex;

    public ForwardIndexMaker(String sourceDir, String targetDir, String crawlFile) {
        this.targetDir = targetDir;
        this.sourceDir = sourceDir;
        this.crawlFile = crawlFile;
        forwardIndex = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(new FileInputStream(sourceDir+"/"+crawlFile));
            String url = scanner.nextLine();

            while (scanner.hasNextLine()) {
                String s = scanner.nextLine().toLowerCase();
                //s = s.replaceAll(" .*[^\\x00-\\x7F].* ", ">:("); //replacing all non-ASCII characters
                s = s.replaceAll("[\\.,!?;'~]","");
                Sentence sentence =  new Sentence(s);
                System.out.println(s);
                List<String> lemmas  = sentence.lemmas();

                for (String l : lemmas) {
                    if(forwardIndex.containsKey(l)) {
                       forwardIndex.put(l,forwardIndex.get(l)+1);
                    } else {
                        forwardIndex.put(l,1);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }

        //System.out.println(forwardIndex);


    }


    public static void main(String[] args) {
        ForwardIndexMaker fim = new ForwardIndexMaker("Crawldata","ForwardIndexDirectory","0a4dcfd8-196a-4ea5-be4b-2a62a85dd118.crawldata");
        new Thread(fim).start();
    }
}

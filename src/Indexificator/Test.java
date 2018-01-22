package Indexificator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        HashMap<String,ArrayList<URLTermFrequencyPair>> invertedIndex;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("TargetDir1/invertedIndex.idx"))) {
            invertedIndex = (HashMap<String,ArrayList<URLTermFrequencyPair>>)ois.readObject();
            System.out.println("***");
            Scanner sc = new Scanner(System.in);
            while (true) {
                String p = sc.nextLine().toLowerCase();
                List l = invertedIndex.get(p);
                Collections.sort(l,(x,y)->((URLTermFrequencyPair)y).score-((URLTermFrequencyPair)x).score);
                //for()
                System.out.println(l);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
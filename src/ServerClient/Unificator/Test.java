package ServerClient.Unificator;

import Indexificator.URLTermFrequencyPair;
import ServerClient.SearchQuery;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        HashMap<String,ArrayList<URLTermFrequencyPair>> invertedIndex;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("TargetF/invertedIndex.idx"))) {
            invertedIndex = (HashMap<String,ArrayList<URLTermFrequencyPair>>)ois.readObject();
            System.out.println("***");
            Unificator unificator = new Unificator(invertedIndex);
            Scanner sc = new Scanner(System.in);
            while (true) {
                String p = sc.nextLine();
                System.out.println(unificator.getResults(new SearchQuery(p)));
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

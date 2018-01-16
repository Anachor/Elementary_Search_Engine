package Indexificator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        HashMap<String,ArrayList<ScoredURL>> hashMap = (HashMap<String,ArrayList<ScoredURL>>)new ObjectInputStream(new FileInputStream("InvertedIndex/invertedIndex.idx")).readObject();
        //System.out.println(new ObjectInputStream(new FileInputStream("InvertedIndex/documentFrequency.freq")).readObject());

        System.out.println(hashMap.get("half"));


    }
}


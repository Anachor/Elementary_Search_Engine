package ServerClient.Unificator;

import Indexificator.URLTermFrequencyPair;
import ServerClient.SearchQuery;
import edu.stanford.nlp.simple.Sentence;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Unificator {
    HashMap<String,ArrayList<URLTermFrequencyPair>> invertedIndex;

    public Unificator(HashMap<String, ArrayList<URLTermFrequencyPair>> invertedIndex) {
        this.invertedIndex = invertedIndex;
    }

    private List<String> processQuery(SearchQuery searchQuery) {
        String s = searchQuery.getQuery().toLowerCase();
        s = s.replaceAll("[\\.,!?;'~/\\-()\\[\\]{}:`\"]","");


        Sentence sentence = new Sentence(s);
        return sentence.lemmas();
    }

    public List<String> getResults(SearchQuery searchQuery) {
        int counter = 0;
        List<ScoredURL> result = new ArrayList<>();
        HashMap<String ,Integer> indexHash = new HashMap<>();
        List<String> tokens = processQuery(searchQuery);

        for(String token : tokens) {
            if(invertedIndex.containsKey(token)) {
                double docFreq = invertedIndex.get(token).size();
                for(URLTermFrequencyPair urlTermFrequencyPair : invertedIndex.get(token)) {
                    if(!indexHash.containsKey(urlTermFrequencyPair.url)) {
                        indexHash.put(urlTermFrequencyPair.url,counter);
                        counter++;
                        result.add(new ScoredURL(urlTermFrequencyPair.url,0));
                    }

                    result.get(indexHash.get(urlTermFrequencyPair.url)).score+=
                            urlTermFrequencyPair.score/docFreq;
                }
            }
        }

        /*System.out.println(result);
        Collections.sort(result);
        System.out.println("Sorted");
        System.out.println(result);*/

        List<String> urlList = new ArrayList<>();
        for(ScoredURL scoredURL : result) {
            urlList.add(scoredURL.url);
        }

        return urlList;

    }
}

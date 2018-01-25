package ServerClient.Unificator;

import Indexificator.URLTermFrequencyPair;
import ServerClient.SearchQuery;
import ServerClient.SearchResult;
import edu.stanford.nlp.simple.Sentence;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Unificator {
    HashMap<String,ArrayList<URLTermFrequencyPair>> invertedIndex;
    SpellChecker spellChecker;

    public Unificator(HashMap<String, ArrayList<URLTermFrequencyPair>> invertedIndex, HashMap<String, Integer> corpus) {
        this.invertedIndex = invertedIndex;
        spellChecker = new SpellChecker(corpus);
    }

    private List<String> processQuery(SearchQuery searchQuery) {
        String s = searchQuery.getQuery().toLowerCase();
        s = s.replaceAll("[\\.,!?;'~/\\-()\\[\\]{}:`\"]","");

        Sentence sentence = new Sentence(s);
        List<String> lemmas = sentence.lemmas();
        List<String> tokens = new ArrayList<>();

        System.out.println(lemmas);

        for (String str: lemmas) {
            System.out.println(spellChecker.dictionary.containsKey(str));
            System.out.println(invertedIndex.containsKey(str));
            tokens.add(spellChecker.getSuggestion(str).get(0));
            System.out.println(spellChecker.getSuggestion(str));
        }

        System.out.println(tokens);
        return lemmas;
    }

    public SearchResult getResults(SearchQuery searchQuery) {
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

        //System.out.println(result);
        Collections.sort(result);
        //System.out.println("Sorted");
        //System.out.println(result);

        List<String> urlList = new ArrayList<>();
        for(ScoredURL scoredURL : result) {
            urlList.add(scoredURL.url);
        }

        return new SearchResult(urlList);

    }
}

package ServerClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchResult implements Serializable{
    List<String> results = new ArrayList<>();

    public SearchResult(SearchQuery query) {
        for (int i=0; i<20; i++)
            this.results.add("www.youtube.com/"+i);
    }

    public SearchResult(List<String> results) {
        this.results = results;
    }

    public List<String> getResults() {
        return results;
    }
}

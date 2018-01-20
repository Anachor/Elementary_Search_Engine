package ServerClient;

import java.io.Serializable;

public class SearchResult implements Serializable{
    String result;

    public SearchResult(SearchQuery query) {
        this.result = query.getQuery() + ": www.youtube.com%2Fwatch%3Fv%3DdQw4w9WgXcQ&usg=AOvVaw0aHtehaphMhOCAkCydRLZU";
    }

    public String getResult() {
        return result;
    }
}

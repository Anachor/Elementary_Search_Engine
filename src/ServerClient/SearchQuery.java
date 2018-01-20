package ServerClient;

import java.io.Serializable;

public class SearchQuery implements Serializable {
    private String query;

    public SearchQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}

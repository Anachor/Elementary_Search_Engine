package Indexer;

public class ScoredURL {
    String url;
    final double score;

    public ScoredURL(String url, double score) {
        this.url = url;
        this.score = score;
    }
}

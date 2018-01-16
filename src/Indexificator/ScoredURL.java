package Indexificator;

import java.io.Serializable;
import java.util.Objects;

class ScoredURL implements Serializable{
    String url;
    final int score;

    public ScoredURL(String url, int score) {
        this.url = url;
        this.score = score;
    }

    @Override
    public String toString() {
        return ("("+url+","+score+")");
    }

    @Override
    public int hashCode() {
        return Objects.hash(url,score);
    }
}

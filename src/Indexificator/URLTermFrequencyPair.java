package Indexificator;

import java.io.Serializable;
import java.util.Objects;

public class URLTermFrequencyPair implements Serializable, Comparable{
    public String url;
    public int score;

    public URLTermFrequencyPair(String url, int score) {
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

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof URLTermFrequencyPair)) {
            throw new UnsupportedOperationException();
        }

        return score - ((URLTermFrequencyPair)o).score;
    }
}

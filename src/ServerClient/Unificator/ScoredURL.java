package ServerClient.Unificator;

import java.util.Objects;

public class ScoredURL implements Comparable {
    String url;
    double score;

    public ScoredURL(String url, double score) {
        this.url = url;
        this.score = score;
    }

    public ScoredURL(String url) {
        this.url = url;
        score = 0;
    }


    @Override
    public int compareTo(Object o) {
        if(!(o instanceof ScoredURL)) {
            throw new UnsupportedOperationException("Can't compare with object of type " + o.getClass());
        }

        ScoredURL temp = (ScoredURL)o;

        if(score<temp.score) return 1;
        else if(score==temp.score) return 0;
        else return -1;
    }

    @Override
    public String toString() {
        return url + " " + score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(url,score);
    }
}

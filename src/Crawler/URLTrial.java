package Crawler;

public class URLTrial {
    int failCount;
    String url;
    int depth;

    public URLTrial(String url) {
        this.url = url;
        failCount = 0;
        depth = 0;
    }

    public URLTrial(String url, int depth) {
        this.failCount = 0;
        this.url = url;
        this.depth = depth;
    }


}

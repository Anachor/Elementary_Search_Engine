package CrawlerForWikipedia;

class URLTrial {
    int failCount;
    String url;
    int depth;

    static String[] patterns = {".php", ".mp3", ".mp4",
            ".jpg", ".png", ".jpeg", ".gif", ".mp4",
            "#", ".pdf", "Category:","catagory:","Help:",
            "help:",".mpeg","File:","file:","Wikipedia:",
            "wikipedia:","Help:","help:","Special:","special:",
            "Talk:","talk:","Template:","template:","Portal:",
            "portal:"};

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

    public static boolean isValid(String url) {
        for (String s: patterns)
            if (url.contains(s))
                return false;
        return true;
    }
}

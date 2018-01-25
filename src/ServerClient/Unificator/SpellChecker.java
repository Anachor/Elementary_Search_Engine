package ServerClient.Unificator;

import java.util.*;

public class SpellChecker {

    HashMap<String, Integer> dictionary;

    public SpellChecker(HashMap<String, Integer> dictionary) {
        this.dictionary = dictionary;
    }

    private static class ScoredString {
        String s;
        int score;

        public ScoredString(String s, int score) {
            this.s = s;
            this.score = score;
        }

        @Override
        public String toString() {
            return "(" + s + "," + score + ")";
        }
    }

    private class RankedStrings implements Comparable {
        String string;
        int frequency;
        int editDistance;

        public RankedStrings(String string, int frequency, int editDistance) {
            this.string = string;
            this.frequency = frequency;
            this.editDistance = editDistance;
        }

        public RankedStrings(ScoredString scoredString, int editDistance) {
            string = scoredString.s;
            frequency = scoredString.score;
            this.editDistance = editDistance;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof RankedStrings)) {
                throw new UnsupportedOperationException("Unsupported Operation Exception >:(");
            }

            RankedStrings rankedStrings = (RankedStrings) o;

            if (editDistance == 0) return -1;
            else if(rankedStrings.editDistance == 0) return 1;

            if (editDistance < rankedStrings.editDistance) return -1;
            else if (editDistance == rankedStrings.editDistance) return rankedStrings.frequency-frequency;
            else return 1;

        }

        @Override
        public String toString() {
            return string + " " + frequency + " " + editDistance;
        }
    }

    final static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();


    /**
     * Generates a list of words with edit distance exactly 1 of string s
     *
     * @param s
     * @return
     */
    private static List<String> generateSuggestionEdit1(String s) {

        List<String> words = new ArrayList<>();

        s = s.toLowerCase();
        //words.add(new ScoredString(s,0));
        //insertions
        for (int i = 0; i <= s.length(); i++) {
            for (char c : alphabet) {
                StringBuilder str = new StringBuilder(s);
                str.insert(i, Character.toString(c));
                words.add(str.toString());
            }
        }

        //Deletions
        for (int i = 0; i < s.length(); i++) {
            StringBuilder str = new StringBuilder(s);
            str.deleteCharAt(i);
            words.add(str.toString());
        }

        //Change a letter
        for (int i = 0; i < s.length(); i++) {
            for (char c : alphabet) {
                StringBuilder str = new StringBuilder(s);
                str.replace(i, i + 1, Character.toString(c));
                words.add(str.toString());
            }

        }

        return words;
    }

    private static List<ScoredString> genarateSuggestionEdit2(String s) {
        List<String> edit1 = generateSuggestionEdit1(s);
        List<ScoredString> words = new ArrayList<>();


        for (String string : edit1) {
            List<String> temp = generateSuggestionEdit1(string);

            for (String p : temp) {
                words.add(new ScoredString(p, 2));
            }
        }

        for (String p : edit1) {
            words.add(new ScoredString(p, 1));
        }

        words.add(new ScoredString(s, 0));


        return words;
    }

    private static HashMap<String, Integer> genarate(String s) {
        HashMap<String, Integer> words = new HashMap<>();
        List<ScoredString> scoredStringsList = genarateSuggestionEdit2(s);

        for (ScoredString scoredString : scoredStringsList) {
            if (scoredString.s == "") continue;

            if (words.containsKey(scoredString.s)) {
                if (words.get(scoredString.s) > scoredString.score) {
                    words.put(scoredString.s, scoredString.score);
                }
            } else {
                words.put(scoredString.s, scoredString.score);
            }
        }


        return words;
    }


    public List<String> getSuggestion(String s) {
        List<String> words = new ArrayList<>();
        List<RankedStrings> rankedStringList = new ArrayList<>();

        HashMap<String, Integer> genaratedWords = genarate(s);
        for (String key : genaratedWords.keySet()) {
            if (dictionary.containsKey(key)) {
                rankedStringList.add(new RankedStrings(key, dictionary.get(key), genaratedWords.get(key)));
            }


        }

        Collections.sort(rankedStringList);

        for (RankedStrings rankedStrings : rankedStringList) {
            words.add(rankedStrings.string);
        }

        return words;
    }

}




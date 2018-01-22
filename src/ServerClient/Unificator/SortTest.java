package ServerClient.Unificator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortTest {
    public static void main(String[] args) {
        List<ScoredURL> l = new ArrayList<>();
        l.add(new ScoredURL("A",1));
        l.add(new ScoredURL("A",3));
        l.add(new ScoredURL("A",-4));
        System.out.println(l);
        Collections.sort(l);
        System.out.println(l);

    }
}

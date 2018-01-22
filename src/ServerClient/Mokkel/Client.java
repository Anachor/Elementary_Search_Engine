package ServerClient.Mokkel;

import ServerClient.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    Scanner scanner;
    NetworkUtil nc;

    public Client(String serverAddress, int serverPort) throws IOException {
        scanner = new Scanner(System.in);
        nc = new NetworkUtil(serverAddress, serverPort);
    }

    public void write(SearchQuery searchQuery) throws IOException {
        System.out.println(searchQuery.getQuery());
        nc.write(searchQuery);
    }

    public SearchResult read() throws IOException, ClassNotFoundException {
        SearchResult s = (SearchResult) nc.read();
        System.out.println(s.getResults());
        return s;
    }

    public void clear() {
        try {
            nc.clear();
        } catch (IOException e) {
            System.out.println("Error clearing stream" + e);
        }
    }

    public void close() {
        try {
            if (nc != null)
                nc.close();
        } catch (IOException e) {
            System.out.println("Error closing: " + e);
        }
    }
}

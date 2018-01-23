package ServerClient.Kamla;

import Indexificator.URLTermFrequencyPair;
import ServerClient.*;
import ServerClient.Unificator.ScoredURL;
import ServerClient.Unificator.Unificator;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerThread implements Runnable {
    private NetworkUtil nc;
    private Unificator unificator;

    public ServerThread(NetworkUtil nc) {
        this.nc = nc;
    }

    public ServerThread(Socket clientSocket,
                        HashMap<String, ArrayList<URLTermFrequencyPair>> invertedIndex)
            throws IOException {
        this.nc = new NetworkUtil(clientSocket);
        this.unificator = new Unificator(invertedIndex);
    }

    @Override
    public void run() {
        try {
            while (true) {
                SearchQuery query = (SearchQuery) nc.read();
                System.out.println(query.getQuery());
                SearchResult s = new SearchResult(unificator.getResults(query));
                nc.write(s);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from network. Closing Connection...");
        } finally {
            try {
                nc.close();
                System.out.println("Connection closed");
            } catch (IOException e) {
                System.out.println("Error closing Connection: " + e);
            }
        }
    }
}

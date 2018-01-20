package ServerClient.Kamla;

import ServerClient.*;
import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {
    private NetworkUtil nc;

    public ServerThread(NetworkUtil nc) {
        this.nc = nc;
    }

    public ServerThread(Socket clientSocket) throws IOException {
        nc = new NetworkUtil(clientSocket);
    }

    @Override
    public void run() {
        try {
            while (true) {
                SearchQuery query = (SearchQuery) nc.read();
                System.out.println(query.getQuery());
                SearchResult s = new SearchResult(query);
                nc.write(s);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from network.");
        } finally {
            try {
                nc.close();
            } catch (IOException e) {
                System.out.println("Error closing Connection: " + e);
            }
        }
    }
}

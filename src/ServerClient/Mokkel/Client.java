package ServerClient.Mokkel;

import ServerClient.*;
import java.io.IOException;
import java.util.Scanner;

public class Client {

    public Client(String serverAddress, int serverPort) {
        NetworkUtil nc = null;
        try {
            Scanner scanner = new Scanner(System.in);
            nc = new NetworkUtil(serverAddress, serverPort);

            while (true) {
                String query = scanner.nextLine();
                SearchQuery searchQuery = new SearchQuery(query);
                nc.write(searchQuery);
                SearchResult s = (SearchResult) nc.read();
                System.out.println(s.getResult());
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading: " + e);
        } finally {
            try {
                if (nc!=null) nc.close();
            } catch (IOException e) {
                System.out.println("Error closing connection: "+e);
            }
        }
    }
    
    public static void main(String args[]) {
        String serverAddress = "127.0.0.1";
        int serverPort = 33333;
        Client client = new Client(serverAddress, serverPort);
    }
}

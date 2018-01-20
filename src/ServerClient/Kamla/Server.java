package ServerClient.Kamla;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;

    Server() {
        try {
            serverSocket = new ServerSocket(33333);
            while (true) {
                //new Thread(new ServerThread(new NetworkUtil(serverSocket.accept()))).start();
                Socket clientSocket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientSocket);
                Thread t = new Thread(serverThread);
                t.start();

                System.out.println("New connection initiated.");
            }
        } catch (IOException e) {
            System.out.println("Error connecting to client: " + e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}

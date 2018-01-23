package ServerClient.Kamla;

import Indexificator.URLTermFrequencyPair;
import ServerClient.Unificator.ScoredURL;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    ServerSocket serverSocket;
    HashMap< String,ArrayList<URLTermFrequencyPair> > invertedIndex;

    Server() {
        try {
            serverSocket = new ServerSocket(33333);
            System.out.println("Server started");
            while (true) {

                init("TargetF");
                //new Thread(new ServerThread(new NetworkUtil(serverSocket.accept()))).start();
                Socket clientSocket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientSocket,invertedIndex);
                Thread t = new Thread(serverThread);
                t.start();

                System.out.println("New connection initiated.");
            }
        } catch (IOException e) {
            System.out.println("Error connecting to client: " + e);
        }
    }

    private void init(String targetDir) throws UnexpectedException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(targetDir + "/" + "invertedIndex.idx"))) {
            invertedIndex = (HashMap<String, ArrayList<URLTermFrequencyPair>>) ois.readObject();
            System.out.println("Server Initialized");
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnexpectedException("Could not read File");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnexpectedException("Class not found. Probably Missing files");
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}

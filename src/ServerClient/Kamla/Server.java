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
import java.util.Scanner;

public class Server {
    ServerSocket serverSocket;
    HashMap < String,ArrayList<URLTermFrequencyPair> > invertedIndex;
    HashMap < String, Integer> corpus;

    Server() {
        try {
            serverSocket = new ServerSocket(33333);
            System.out.println("Server started");
            init("TargetF");
            while (true) {
                //new Thread(new ServerThread(new NetworkUtil(serverSocket.accept()))).start();
                Socket clientSocket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientSocket,invertedIndex, corpus);
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
        BuildCorpus();
    }

    public static void main(String[] args) {
        new Server();
    }

    void BuildCorpus() {
        corpus = new HashMap<>();
        for (String s: invertedIndex.keySet()) {
            int count = 0;
            for (URLTermFrequencyPair pr: invertedIndex.get(s))
                count += pr.score;
            corpus.put(s, count);
        }
        System.out.println("Corpus built");
    }
}

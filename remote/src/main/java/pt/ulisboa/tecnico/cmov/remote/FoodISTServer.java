package pt.ulisboa.tecnico.cmov.remote;

import java.io.*;
import java.net.*;

public class FoodISTServer {

    public static void main(String[] args) {

        State state = new State();

        try {

            ServerSocket serverSocket = new ServerSocket(8000);
            System.out.println("Server is listening on port " + 8000);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                FoodISTServerThread thread = new FoodISTServerThread(clientSocket, state);
                new Thread(thread).start();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

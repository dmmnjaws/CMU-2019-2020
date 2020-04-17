package pt.ulisboa.tecnico.cmov.remote;

import java.io.*;
import java.net.*;

/**
 * This program demonstrates a simple TCP/IP socket server that echoes every
 * message from the client in reversed form.
 * This server is multi-threaded.
 *
 * @author www.codejava.net
 */
public class FoodISTServer {

    public static void main(String[] args) throws IOException {

        State state = new State();

        while (true) {

            Socket clientSocket = null;

            try {


                ServerSocket serverSocket = new ServerSocket(8000);
                System.out.println("Server is listening on port " + 8000);


                clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

                FoodISTServerThread thread = new FoodISTServerThread(state, inputStream, outputStream, clientSocket);
                new Thread(thread).start();
            } catch (IOException ex) {
                clientSocket.close();
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}


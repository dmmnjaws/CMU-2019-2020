package pt.ulisboa.tecnico.cmov.remote;

import java.io.*;
import java.net.*;

import pt.ulisboa.tecnico.cmov.library.Dish;

/**
 * This thread is responsible to handle client connection.
 *
 * @author www.codejava.net
 */
public class FoodISTServerThread implements Runnable {

    private Socket socket;
    private State state;

    public FoodISTServerThread(Socket socket, State state) {
        this.socket = socket;
        this.state = state;
    }

    public void run() {
        try {

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            ObjectInputStream inputStream = new ObjectInputStream(in);
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            String command;

            command = (String) inputStream.readObject();

            switch (command){

                case "loadState":{
                    outputStream.writeObject(this.state.getDiningOptions());
                }
            }

        } catch (Exception ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

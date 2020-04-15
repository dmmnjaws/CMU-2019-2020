package pt.ulisboa.tecnico.cmov.remote;

import java.io.*;
import java.net.*;

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

            String command = "";
            String[] tokens = command.split(" ");

            while (true){

                switch (tokens[0]){
                    case "Hello":
                        String hello = state.Hello();
                        break;
                }

            }
        } catch (Exception ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

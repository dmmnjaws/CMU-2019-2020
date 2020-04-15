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

            InputStream inFromServer = socket.getInputStream();
            ObjectInputStream in = new ObjectInputStream(inFromServer);
            String command;

            command = (String) in.readObject();

            switch (command){
                case "Hello": {
                    String hello = state.Hello();
                    break;
                }
                case "DishName":{
                    System.out.println(((Dish) in.readObject()).getName());
                }
            }
            
        } catch (Exception ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

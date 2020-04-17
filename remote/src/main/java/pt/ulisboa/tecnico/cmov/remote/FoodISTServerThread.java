package pt.ulisboa.tecnico.cmov.remote;

import java.io.*;
import java.net.*;

import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishImage;

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

                case "loadState":
                    outputStream.writeObject(this.state.getDishes());
                    System.out.println("LoadState: State sent to client");
                    break;

                case "addDish":
                    state.addDish( (Dish) inputStream.readObject() );
                    System.out.println("AddDish: Dish added to server");
                    break;

                case "addDishImage":
                    state.addDishImage((DishImage) inputStream.readObject());
                    System.out.println("AddDishImage: Added dish image to server");
                    break;

                case "addRating":
                    String diningOptionName = ((String) inputStream.readObject());
                    String dishName = ((String) inputStream.readObject());
                    String username = ((String) inputStream.readObject());
                    float rating = ((float) inputStream.readObject());
                    state.addRating(diningOptionName, dishName, username,rating);
                    System.out.println("AddDishImage: Added dish image to server");
                    break;
            }

        } catch (Exception ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

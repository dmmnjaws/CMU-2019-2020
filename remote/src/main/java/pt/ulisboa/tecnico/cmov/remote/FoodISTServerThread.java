package pt.ulisboa.tecnico.cmov.remote;

import java.io.*;
import java.net.*;

import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishImage;

public class FoodISTServerThread implements Runnable {

    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Socket clientSocket;
    private State state;

    public FoodISTServerThread(State state, ObjectInputStream inputStream,
                               ObjectOutputStream outputStream, Socket clientSocket) {

        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.state = state;
    }

    public void run() {

        while(true) {
            try {
                String command;
                command = (String) this.inputStream.readObject();

                if(command.equals("Exit"))
                {
                    this.clientSocket.close();
                    System.out.println("Connection closed");
                    break;
                }

                switch (command) {
                    case "loadState":
                        this.outputStream.writeObject(this.state.getDishes());
                        System.out.println("client called loadState request.");
                        break;
                    case "addDish":
                        System.out.println("client called addDish request.");
                        this.state.addDish((Dish) this.inputStream.readObject());

                        break;
                    case "addDishImage":
                        this.state.addDishImage((DishImage) this.inputStream.readObject());
                        System.out.println("client called addDishImage request.");
                        break;
                    case "addRating":
                        String diningOptionName = (String) this.inputStream.readObject();
                        String dishName = (String) this.inputStream.readObject();
                        String username = (String) this.inputStream.readObject();
                        float rating = (float) this.inputStream.readObject();
                        this.state.addRating(diningOptionName, dishName, username, rating);
                        System.out.println("client called addRating request.");
                        break;
                }

            } catch (Exception ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        try
        {
            this.inputStream.close();
            this.outputStream.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

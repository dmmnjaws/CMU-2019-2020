package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishImage;

public class AddRatingRemotely extends AsyncTask {

    private String diningOptions;
    private String dishName;
    private String username;
    private float rating;

    public AddRatingRemotely(String diningOptions, String dishName, String username, float rating){
        this.diningOptions = diningOptions;
        this.dishName = dishName;
        this.username = username;
        this.rating = rating;
    }

    protected Object doInBackground(Object[] objects) {
        try {

            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            outputStream.writeObject("addRating");
            outputStream.writeObject(this.diningOptions);
            outputStream.writeObject(this.dishName);
            outputStream.writeObject(this.username);
            outputStream.writeObject(this.rating);

            clientSocket.close();
            Log.d("DEBUG:", "DEBUG - DID ASYNC SERVER READ");
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }
}


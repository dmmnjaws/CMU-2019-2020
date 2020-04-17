package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishesView;

public class AddDishRemotely extends AsyncTask {

    private Dish dish;

    public AddDishRemotely(Dish dish){
        this.dish = dish;
    }

    protected Object doInBackground(Object[] objects) {
        try {

            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream.writeObject("addDish");
            outputStream.writeObject(dish);
            clientSocket.close();
            Log.d("DEBUG:", "DEBUG - DID ASYNC SERVER READ");
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }
}


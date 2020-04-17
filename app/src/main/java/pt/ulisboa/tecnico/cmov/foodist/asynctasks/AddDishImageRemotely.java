package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishImage;

public class AddDishImageRemotely extends AsyncTask {

    private DishImage newDishImage;

    public AddDishImageRemotely(DishImage newDishImage){
        this.newDishImage = newDishImage;
    }

    protected Object doInBackground(Object[] objects) {
        try {

            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            outputStream.writeObject("addDishImage");
            outputStream.writeObject(this.newDishImage);

            clientSocket.close();
            Log.d("DEBUG:", "DEBUG - DID ASYNC SERVER READ");
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }
}


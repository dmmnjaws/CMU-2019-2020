package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.library.DishImage;

public class AddDishImageRemotely extends AsyncTask {

    private DishImage newDishImage;
    private byte[] imageBytes;

    public AddDishImageRemotely(DishImage newDishImage, byte[] imageBytes){
        this.newDishImage = newDishImage;
        this.imageBytes = imageBytes;
    }

    protected Object doInBackground(Object[] objects) {
        try {

            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream.writeObject("addDishImage");
            outputStream.writeObject(this.newDishImage);
            outputStream.writeObject(this.imageBytes);
            clientSocket.close();
            Log.d("DEBUG:", "DEBUG - DID ASYNC SERVER READ");
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }
}


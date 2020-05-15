package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishImage;

public class AddDishRemotely extends AsyncTask {

    private Dish dish;
    private DishImage icon;
    private byte[] imageBytes;

    public AddDishRemotely(Dish dish, DishImage icon, byte[] imageBytes)
    {
        this.dish = dish;
        this.icon = icon;
        this.imageBytes = imageBytes;
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

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(this.icon != null){
            System.out.println("Icon: " + this.icon.getImageId());
            AddDishImageRemotely newImage = new AddDishImageRemotely(this.icon, this.imageBytes);
            newImage.execute();
        }
    }
}


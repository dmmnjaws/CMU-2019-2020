package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.foodist.Cache;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.library.DishImage;
import pt.ulisboa.tecnico.cmov.library.DishesView;

public class GetImageRemotely extends AsyncTask {

    private DishImage dishImage;
    private Cache cache;
    private ImageView imageView;
    private boolean thumbnail;

    public GetImageRemotely(DishImage dishImage, Cache cache, ImageView view, boolean thumbnail){

        this.dishImage = dishImage;
        this.cache = cache;
        this.imageView = view;
        this.thumbnail = thumbnail;
    }

    protected Object doInBackground(Object[] objects) {
        try {

            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream.writeObject("getImage");
            outputStream.writeObject(this.dishImage.getDiningPlace() + this.dishImage.getDishName() + this.dishImage.getImageId());
            byte[] imageBytes = (byte[]) inputStream.readObject();
            System.out.println("test1" + imageBytes.length);
            this.cache.insertImageCache(this.dishImage, imageBytes);
            clientSocket.close();
            Log.d("DEBUG:", "DEBUG - RECEIVED IMAGE AND PLACED ON CACHE");
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (this.imageView != null && thumbnail == false) {
            this.imageView.setImageBitmap(this.cache.getImageFromCache(this.dishImage));
        } else if (this.imageView != null) {
            System.out.println(this.cache.getImageFromCache(this.dishImage)==null);
            this.imageView.setImageBitmap(Bitmap.createScaledBitmap(this.cache.getImageFromCache(this.dishImage),50, 50, false));
        }
        Log.d("DEBUG:", "DEBUG - GOT IMAGE FROM CACHE");
    }
}


package pt.ulisboa.tecnico.cmov.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class DishImage implements Serializable {

    private int imageId;
    private String dishName;
    private String diningPlace;
    private String uploaderUsername;

    public DishImage(String uploaderUsername, String diningPlace, String dishName){

        this.uploaderUsername = uploaderUsername;
        this.diningPlace = diningPlace;
        this.dishName = dishName;
    }

    public int getImageId() {
        return this.imageId;
    }

    public String getDishName(){ return this.dishName;}

    public String getDiningPlace(){ return this.diningPlace;}

    public void setImageId(int imageId) { this.imageId = imageId; }

    public String getUploaderUsername(){
        return this.uploaderUsername;
    }

}

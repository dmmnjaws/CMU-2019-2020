package pt.ulisboa.tecnico.cmov.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class DishImage implements Serializable {

    //TO DO: ADD BITMAP
    private byte[] image;
    private int imageId;
    private String dishName;
    private String diningPlace;
    private String uploaderUsername;

    public DishImage(String uploaderUsername, Bitmap image, String diningPlace, String dishName){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.image = stream.toByteArray();

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

    public Bitmap getBitmap(){ return BitmapFactory.decodeByteArray(this.image, 0, this.image.length); }
}

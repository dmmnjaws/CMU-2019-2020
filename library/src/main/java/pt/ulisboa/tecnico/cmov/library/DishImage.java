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

    public DishImage(int imageId, String uploaderUsername, Bitmap image){
        this.imageId = imageId;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.image = stream.toByteArray();

        this.uploaderUsername = uploaderUsername;
    }

    public int getImageId() {
        return this.imageId;
    }

    public String getDishName(){ return this.dishName;}

    public String getDiningPlace(){ return this.diningPlace;}

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public void setDiningPlace(String diningPlace) { this.diningPlace = diningPlace; }

    public String getUploaderUsername(){
        return this.uploaderUsername;
    }

    public Bitmap getBitmap(){ return BitmapFactory.decodeByteArray(this.image, 0, this.image.length); }
}

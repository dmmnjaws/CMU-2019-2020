package pt.ulisboa.tecnico.cmov.library;

import android.graphics.Bitmap;

public class DishImage {

    //TO DO: ADD BITMAP
    private Bitmap image;
    private int imageId;
    private String dishName;
    private String diningPlace;
    private String uploaderUsername;

    public DishImage(int imageId, String uploaderUsername, Bitmap image){
        this.imageId = imageId;
        this.image = image;
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

    public Bitmap getBitmap(){ return this.image; }
}

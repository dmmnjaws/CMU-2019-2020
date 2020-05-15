package pt.ulisboa.tecnico.cmov.foodist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import pt.ulisboa.tecnico.cmov.library.DishImage;

public class Cache {
    private LruCache<String, Bitmap> cacheImage;

    public Cache(){
        this.cacheImage = new LruCache<>(100*1024*1024); //100 MB capacity
    }

    public synchronized void insertImageCache(DishImage newImage, byte [] imageBytes){

        this.cacheImage.put(newImage.getDiningPlace() + newImage.getDishName() + newImage.getImageId(),
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));

    }

    public synchronized Bitmap getImageFromCache(DishImage dishImage){

        return this.cacheImage.get(dishImage.getDiningPlace() + dishImage.getDishName()
                + dishImage.getImageId());
    }

}
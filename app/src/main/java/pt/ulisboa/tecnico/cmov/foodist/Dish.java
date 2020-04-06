package pt.ulisboa.tecnico.cmov.foodist;

import java.io.Serializable;
import java.util.ArrayList;

public class Dish implements Serializable {

    private String name;
    private String cost;
    private float rating;
    private int thumbnailId;
    private ArrayList<DishImage> dishImages;

    public Dish(String name, String cost, float rating, int thumbnailId){
        this.name = name;
        this.cost = cost;
        this.rating = rating;
        this.thumbnailId = thumbnailId;
    }

    public String getName() {
        return this.name;
    }

    public String getCost() {
        return this.cost;
    }

    public float getRating() {
        return this.rating;
    }

    public int getThumbnailId(){
        return this.thumbnailId;
    }
}

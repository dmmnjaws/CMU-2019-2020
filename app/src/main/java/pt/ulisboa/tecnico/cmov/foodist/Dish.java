package pt.ulisboa.tecnico.cmov.foodist;

import java.util.ArrayList;

public class Dish {

    private String name;
    private String cost;
    private float rating;
    private int thumbnailId;
    private ArrayList<DishImage> dishImages;

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

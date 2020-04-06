package pt.ulisboa.tecnico.cmov.foodist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dish implements Serializable {

    private String diningPlace;
    private String name;
    private String cost;
    private Map<String, Float> voterRatings;
    private float rating;
    private int thumbnailId;
    private ArrayList<DishImage> dishImages;

    public Dish(String name, String cost, float rating, int thumbnailId, String userName){
        this.name = name;
        this.cost = cost;
        this.rating = rating;
        this.thumbnailId = thumbnailId;
        this.voterRatings = new HashMap<>();
        this.voterRatings.put(userName,rating);
    }

    public String getName() {
        return this.name;
    }

    public String getCost() {
        return this.cost;
    }

    public float getRating() { return this.rating/this.voterRatings.size(); }

    public int getThumbnailId(){
        return this.thumbnailId;
    }

    public void setDiningPlace(String name){ this.diningPlace = name; }

    public String getDiningPlace(){ return this.diningPlace; }

    public void addRating(String user, float rating){
        if(voterRatings.containsKey(user)){
            this.rating -= voterRatings.get(user);
            this.rating += rating;
            voterRatings.put(user,rating);
        }else{
            this.rating += rating;
            voterRatings.put(user,rating);
        }
    }

    public float getUserRating(String user){
        if(this.voterRatings.get(user)==null){
            return 0;
        }else{
            return this.voterRatings.get(user);
        }
    }
}

package pt.ulisboa.tecnico.cmov.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
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
    private Map<String, Boolean> categories;

    private ArrayList<DishImage> dishImages;

    public Dish(String name, String cost, float rating, String userName){
        this.name = name;
        this.cost = cost + " €";
        this.rating = rating;
        this.voterRatings = new HashMap<String, Float>();
        this.voterRatings.put(userName,rating);
        this.dishImages = new ArrayList<>();

        this.categories = new HashMap<>();
        this.categories.put("Vegetarian", false);
        this.categories.put("Vegan", false);
        this.categories.put("Fish", false);
        this.categories.put("Meat", false);
    }

    public String getName() {
        return this.name;
    }

    public String getCost() {
        return this.cost;
    }

    public float getRating() { return this.rating/this.voterRatings.size(); }

    public void setDiningPlace(String name){ this.diningPlace = name; }

    public String getDiningPlace(){ return this.diningPlace; }

    public Map<String, Float> getVoterRatings(){ return this.voterRatings; }

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

    public synchronized void addImage(DishImage newDishImage){
        newDishImage.setImageId(this.dishImages.size());
        this.dishImages.add(newDishImage);
    }

    public ArrayList<DishImage> getImages(){
        return this.dishImages;
    }

    public DishImage getDishImage(int imageId){
        for (DishImage dishImage: this.dishImages){
            if (dishImage.getImageId() == imageId){
                return dishImage;
            }
        }

        return null;
    }

    public Map<String, Boolean> getCategories() {
        return this.categories;
    }

    public void setCategories(Map<String, Boolean> categories) {
        this.categories = categories;
    }

    public void setCategories(boolean isVegetarian, boolean isVegan, boolean isMeat, boolean isFish){
        this.categories.put("Vegetarian", isVegetarian);
        this.categories.put("Vegan", isVegan);
        this.categories.put("Fish", isFish);
        this.categories.put("Meat", isMeat);
    }
}

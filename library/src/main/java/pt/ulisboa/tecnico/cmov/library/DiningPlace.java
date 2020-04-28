package pt.ulisboa.tecnico.cmov.library;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class DiningPlace implements Serializable {

    private String name;
    private String address;
    private String campus;

    private byte[] thumbnail;
    private ArrayList<Dish> dishes;
    private String[] schedule;
    private String queueTime;
    private String walkingTime;
    private double[] coordinates;

    private float rating;

    public DiningPlace(String name, String address, Bitmap thumbnail, String[] schedule, String campus, double v, double v1){
        this.name = name;
        this.address = address;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap.createScaledBitmap(thumbnail, 50, 50, false).compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.thumbnail = stream.toByteArray();

        this.dishes = new ArrayList<>();
        this.schedule = schedule;
        this.queueTime = "... calculating ...";
        this.walkingTime = "... calculating ...";
        this.campus = campus;
        this.coordinates = new double[]{v,v1};

        this.rating = 0;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public Bitmap getThumbnail(){ return BitmapFactory.decodeByteArray(this.thumbnail, 0, this.thumbnail.length); }

    public ArrayList<Dish> getDishes() { return this.dishes; }

    public ArrayList<Dish> getDishesBasedOnPreference(Map<String, Boolean> preferences){
        ArrayList<Dish> selection = new ArrayList<>();
        for (Dish dish : this.dishes){
            Map<String, Boolean> categories = dish.getCategories();
            if ((!preferences.get("Vegetarian") && categories.get("Vegetarian")) ||
                    (!preferences.get("Gluten-Free") && categories.get("Gluten-Free")) ||
                    (!preferences.get("Meat") && categories.get("Meat")) ||
                    (!preferences.get("Fish") && categories.get("Fish"))){

                break;

            }
            selection.add(dish);

        }
        return selection;
    }

    public Dish getDish(String dishName){

        for (Dish dish: this.dishes){
            if (dish.getName().equals(dishName)){
                return dish;
            }
        }

        return null;
    }

    public String getSchedule(int category) { return this.schedule[category]; }

    public String getQueueTime() { return this.queueTime; }

    public String getWalkingTime() { return this.walkingTime; }

    public String getCampus() { return this.campus; }

    public double[] getCoordinates(){ return this.coordinates; }

    public LatLng getCoordinatesLatLng(){ return new LatLng(this.coordinates[0], this.coordinates[1]); }

    public synchronized void addDish(Dish dish){
        dish.setDiningPlace(this.name);
        this.dishes.add(dish);
    }

    public void setDishes(ArrayList<Dish> dishes){
        this.dishes = dishes;
    }

    public void setQueueTime(String queueTime){
        this.queueTime = queueTime;
    }

    public void setWalkingTime(String walkingTime){
        this.walkingTime = walkingTime;
    }

    public void calculateRating(){

        float rating = 0;

        for (Dish dish: dishes){
            rating += dish.getRating();
        }

        this.rating = rating;
    }

    public float getRating(){
        calculateRating();
        return this.rating / this.dishes.size(); }
}

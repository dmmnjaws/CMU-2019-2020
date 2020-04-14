package pt.ulisboa.tecnico.cmov.foodist;

import java.io.Serializable;
import java.util.ArrayList;

public class DiningOption implements Serializable {

    private String name;
    private String address;
    private String campus;
    //TO DO: ADD BITMAP
    private int imageId;
    private ArrayList<Dish> dishes;
    private String[] schedule;
    private String queueTime;


    public DiningOption(String name, String address, int imageId, String[] schedule, String campus){
        this.name = name;
        this.address = address;
        this.imageId = imageId;
        this.dishes = new ArrayList<>();
        this.schedule = schedule;
        this.queueTime = "0";
        this.campus = campus;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public int getImageId() {
        return this.imageId;
    }

    public ArrayList<Dish> getDishes() { return this.dishes; }

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

    public String getCampus() { return this.campus; }

    public void addDish(Dish dish){
        dish.setDiningPlace(this.name);
        this.dishes.add(dish);
    }
}

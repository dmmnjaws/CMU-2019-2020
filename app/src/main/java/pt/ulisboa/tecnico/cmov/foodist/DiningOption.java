package pt.ulisboa.tecnico.cmov.foodist;

import java.io.Serializable;
import java.util.ArrayList;

public class DiningOption implements Serializable {

    private String name;
    private String address;
    private int imageId;
    private ArrayList<Dish> dishes;
    private String[] schedule;
    private String queueTime;

    public DiningOption(String name, String address, int imageId, String[] schedule){
        this.name = name;
        this.address = address;
        this.imageId = imageId;
        this.dishes = new ArrayList<>();
        this.schedule = schedule;
        this.queueTime = "0";
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

    public String getSchedule(int category) { return this.schedule[category]; }

    public String getQueueTime() { return this.queueTime; }

    public void addDish(Dish dish){
        dish.setDiningPlace(this.name);
        this.dishes.add(dish);
    }
}

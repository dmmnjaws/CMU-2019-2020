package pt.ulisboa.tecnico.cmov.foodist;

import java.io.Serializable;
import java.util.ArrayList;

public class DiningOption implements Serializable {

    private String name;
    private String address;
    private int imageId;
    private ArrayList<Dish> dishes;

    public DiningOption(String name, String address, int imageId){
        this.name = name;
        this.address = address;
        this.imageId = imageId;
        this.dishes = new ArrayList<>();
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

    public void addDish(Dish dish){
       this.dishes.add(dish);
    }
}

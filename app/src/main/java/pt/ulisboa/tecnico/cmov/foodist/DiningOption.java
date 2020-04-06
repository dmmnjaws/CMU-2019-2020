package pt.ulisboa.tecnico.cmov.foodist;

import java.util.ArrayList;

public class DiningOption {

    private String name;
    private String address;
    private int imageId;
    private ArrayList<Dish> dishes;

    public DiningOption(String name, String address, int imageId){
        this.name = name;
        this.address = address;
        this.imageId = imageId;
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
}

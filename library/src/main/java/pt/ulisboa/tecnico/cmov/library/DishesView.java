package pt.ulisboa.tecnico.cmov.library;

import java.io.Serializable;
import java.util.ArrayList;

public class DishesView implements Serializable {

    private String campus;
    private String diningPlace;
    private ArrayList<Dish> dishes;

    public DishesView(String campus, String diningPlace, ArrayList<Dish> dishes){
        this.campus = campus;
        this.diningPlace = diningPlace;
        this.dishes = new ArrayList<>();
    }

    public String getCampus(){ return this.campus; }
    public String getDiningPlace() { return this.diningPlace; }
    public ArrayList<Dish> getDishes() { return this.dishes; }

    public void AddDish(Dish dish){
        this.dishes.add(dish);
    }

}

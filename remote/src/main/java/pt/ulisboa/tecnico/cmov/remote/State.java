package pt.ulisboa.tecnico.cmov.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.library.DiningPlace;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishImage;
import pt.ulisboa.tecnico.cmov.library.DishesView;

public class State {

    private ArrayList<DishesView> dishesViews;

    public State(){
        populate();
    }

    public void populate(){

        this.dishesViews = new ArrayList<>();
        //FOR TEST PURPOSES:
        this.dishesViews.add(new DishesView("Alameda", "Frankie Hot Dogs", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Alameda", "Ali Baba Kebab Haus", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Alameda", "Sena - Pastelaria e Restaurante", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Alameda", "Santorini Coffee", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Alameda", "Kokoro Ramen Bar", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Taguspark", "Red Bar", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Taguspark", "GreenBar Tagus", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Taguspark", "Momentum", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Taguspark", "Panorâmico by Marlene Vieira", new ArrayList<Dish>()));


    }

    public void addDish(Dish dish){

        for (DishesView dishesView: dishesViews){
            if (dishesView.getDiningPlace().equals(dish.getDiningPlace())){
                dishesView.AddDish(dish);
            }
        }

    }

    public void addDishImage(DishImage newDishImage){

        getDish(newDishImage.getDiningPlace(),newDishImage.getDishName()).addImage(newDishImage);

    }

    public void addRating(String diningOptionName, String dishName, String username, float rating){
        getDish(diningOptionName, dishName).addRating(username, rating);
    }

    public Dish getDish(String diningPlace, String dishName){

        for(DishesView dishesView : this.dishesViews){
            if (dishesView.getDiningPlace().equals(diningPlace)){
                for(Dish dish : dishesView.getDishes()){
                    if(dish.getName().equals(dishName)){
                        return dish;
                    }
                }
            }
        }

        return null;
    }

    public ArrayList<DishesView> getDishes(){ return this.dishesViews; }

}
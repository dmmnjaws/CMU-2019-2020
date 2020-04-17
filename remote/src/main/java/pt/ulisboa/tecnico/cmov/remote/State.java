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
        this.dishesViews.add(new DishesView("Alameda", "Copacabana", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Alameda", "Restaurante do Santos G", new ArrayList<Dish>()));
        this.dishesViews.add(new DishesView("Taguspark", "Uganda Ristoranti", new ArrayList<Dish>()));

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
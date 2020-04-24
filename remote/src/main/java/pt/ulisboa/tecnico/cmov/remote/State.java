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
    private Map<String, String> usernamesPasswords;

    public State(){
        populate();
    }

    public void populate(){

        this.dishesViews = new ArrayList<>();
        this.usernamesPasswords = new HashMap<>();
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
        this.usernamesPasswords.put("", "");
        this.usernamesPasswords.put("johny", "1234567890");
        this.usernamesPasswords.put("Maria", "0987654321");

    }

    public boolean authenticate(String usernameAuthenticate, String passwordAuthenticate){

        String password = this.usernamesPasswords.get(usernameAuthenticate);
        if (password == null || !password.equals(passwordAuthenticate)){
            return false;
        }

        return true;
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

    public boolean createAccount(String newUsername, String newPassword){

        if(usernamesPasswords.containsKey(newUsername)){
            return false;
        } else{
            usernamesPasswords.put(newUsername, newPassword);
            return true;
        }
    }

    public void joinQueue(String usernameIn, String beaconNameIn, long timestampIn){

    }

    public void leaveQueue(String usernameOut, String beaconNameOut, long timestampOut){

    }


}
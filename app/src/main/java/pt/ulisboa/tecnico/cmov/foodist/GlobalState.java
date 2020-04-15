package pt.ulisboa.tecnico.cmov.foodist;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.foodist.asynctasks.StateLoader;
import pt.ulisboa.tecnico.cmov.library.DiningPlace;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishImage;

public class GlobalState extends Application {

    private String username;
    private String password;
    private Bitmap profilePicture;
    private String[] campuses;
    private boolean loggedIn;
    private String[] categories;
    private Map<String, ArrayList<DiningPlace>> diningOptions;
    private int actualCategory;

    public GlobalState(){
        this.categories = new String[] {"Student", "Researcher", "Professor", "Staff", "General Public"};
        this.actualCategory = 0;
        this.loggedIn = false;

        // TO DO fetch everything below from MySQLite database:
        this.diningOptions = new HashMap<>();
        this.diningOptions.put("Alameda", new ArrayList<DiningPlace>());
        this.diningOptions.put("Taguspark", new ArrayList<DiningPlace>());
        this.campuses = new String[] {"Alameda", "Taguspark"};

    }

    public void login(String username, String password) {

        // TO DO authentication
        this.username = username;
        this.password = password;
        this.loggedIn = true;

        StateLoader stateLoader = new StateLoader();
        stateLoader.execute();
        while (stateLoader.getStatus() != AsyncTask.Status.FINISHED){
        }
        this.diningOptions = stateLoader.getState();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActualCategory(String actualCategory){

        for(int i=0; i<categories.length; i++){
            if (categories[i] == actualCategory) {
                this.actualCategory = i;
            }
        }

    }

    public DiningPlace getDiningOption(String campus, String diningOptionName){

        for (DiningPlace diningPlace : this.diningOptions.get(campus)) {
            if(diningPlace.getName().equals(diningOptionName)){
                return diningPlace;
            }
        }

        return null;
    }

    public int getDiningOptionIndex(String campus, String diningOptionName){

        int index = 0;

        for (DiningPlace diningPlace : this.diningOptions.get(campus)) {
            if(diningPlace.getName().equals(diningOptionName)){
                return index;
            }
            index++;
        }

        return index;
    }

    public Dish getDish(String campus, String diningOptionName, String dishName){

        for (DiningPlace diningPlace : this.diningOptions.get(campus)) {
            if(diningPlace.getName().equals(diningOptionName)){
                for (Dish dish: diningPlace.getDishes()) {
                    if(dish.getName().equals(dishName)){
                        return dish;
                    }
                }
            }
        }

        return null;
    }

    public int getDishIndex(String campus, String diningOptionName, String dishName){

        int index = 0;

        for (DiningPlace diningPlace : this.diningOptions.get(campus)) {
            if(diningPlace.getName().equals(diningOptionName)){
                for (Dish dish: diningPlace.getDishes()) {
                    if(dish.getName().equals(dishName)){
                        return index;
                    }
                    index++;
                }
            }
        }

        return index;
    }

    public String[] getDiningOptionNames(String campus){

        String[] result = new String[diningOptions.get(campus).size()];
        int index = 0;

        for(DiningPlace diningPlace : this.diningOptions.get(campus)) {
            result[index] = diningPlace.getName();
            index++;
        }

        return result;
    }

    public String[] getDishNames(DiningPlace diningPlace){

        String[] result = new String[diningPlace.getDishes().size()];
        int index = 0;

        for(Dish dish: diningPlace.getDishes()) {
            result[index] = dish.getName();
            index++;
        }

        return result;
    }

    public String[] getCategories(){
        return this.categories;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public int getActualCategoryIndex(){
        return this.actualCategory;
    }

    public String getActualCategory(){
        return this.categories[this.actualCategory];
    }

    public ArrayList<DiningPlace> getDiningOptions(String campus) { return this.diningOptions.get(campus); }

    public DishImage getDishImage(String campus, String diningOptionName, String dishName, int imageId){

        Dish dish = getDish(campus, diningOptionName, dishName);
        return dish.getDishImage(imageId);
    }

    public Bitmap getProfilePicture() { return this.profilePicture; }

    public String[] getCampuses(){ return this.campuses; }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void addDiningOption(DiningPlace diningPlace){
        this.diningOptions.get(diningPlace.getCampus()).add(diningPlace);
    }

    public boolean isLoggedIn(){
        return this.loggedIn;
    }

    public Bitmap customBitMapper(int imageId) {

        try {
            return BitmapFactory.decodeResource(getResources(), imageId);

        } catch (Exception e) {
            return null;

        }
    }

}

package pt.ulisboa.tecnico.cmov.foodist;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.foodist.asynctasks.StateLoader;
import pt.ulisboa.tecnico.cmov.library.DiningPlace;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishImage;
import pt.ulisboa.tecnico.cmov.library.DishesView;

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

    }

    public void login(String username, String password) {

        // TO DO authentication
        this.username = username;
        this.password = password;
        this.loggedIn = true;

        if (this.diningOptions == null){ populate(); }


        StateLoader stateLoader = new StateLoader(this);
        stateLoader.execute();


    }

    public synchronized void setState(ArrayList<DishesView> dishesViews){
        if (dishesViews != null){
            for(DishesView dishesView: dishesViews){
                (getDiningOption(dishesView.getCampus(), dishesView.getDiningPlace())).setDishes(dishesView.getDishes());
            }
        }
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

    public void addDish(String campus, String diningOptionName, Dish dish){
        getDiningOption(campus, diningOptionName).addDish(dish);
    }

    public void addDishImage(Dish dish, DishImage newDishImage){
        dish.addImage(newDishImage);
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

    public void populate(){

        //TO DO: TAKING TOO LONG, SOLVE.

        this.diningOptions = new HashMap<>();
        this.diningOptions.put("Alameda", new ArrayList<DiningPlace>());
        this.diningOptions.put("Taguspark", new ArrayList<DiningPlace>());
        this.campuses = new String[] {"Alameda", "Taguspark"};

        String[] defaultSchedule = new String[] {"Mon - Fri, 11:00 - 22:00", "Mon - Fri, 8:00 - 22:00", "Mon - Fri, 8:00 - 00:00", "Mon - Fri, 11:00 - 17:00", "Mon, - Fri, 8:00 - 22:00"};
        addDiningOption(new DiningPlace("Red Bar", "Av. Prof. Dr. Cavaco Silva 13", customBitMapper(R.drawable.redbar), defaultSchedule, "Taguspark", 38.736578,-9.302192));
        addDiningOption(new DiningPlace("GreenBar Tagus", "Av. Prof. Dr. Cavaco Silva 13", customBitMapper(R.drawable.greenbar), defaultSchedule, "Taguspark", 38.738011, -9.303076));
        addDiningOption(new DiningPlace("Momentum", "Parque de Ciências e Tecnologia Núcleo Central", customBitMapper(R.drawable.momentum), defaultSchedule, "Taguspark", 38.740156, -9.304929));
        addDiningOption(new DiningPlace("Panorâmico by Marlene Vieira", "Avenida Dr, Av. Jacques Delors 1 401", customBitMapper(R.drawable.panoramico), defaultSchedule, "Taguspark", 38.740261, -9.304023));
        addDiningOption(new DiningPlace("Frankie Hot Dogs", "R. Alves Redol 13", customBitMapper(R.drawable.frankie), defaultSchedule, "Alameda", 38.736957, -9.140762));
        addDiningOption(new DiningPlace("Ali Baba Kebab Haus", "R. Alves Redol 3", customBitMapper(R.drawable.kebab), defaultSchedule, "Alameda", 38.735676, -9.140726));
        addDiningOption(new DiningPlace("Sena - Pastelaria e Restaurante", "Av. de António José de Almeida 14", customBitMapper(R.drawable.sena), defaultSchedule, "Alameda", 38.737964, -9.138673));
        addDiningOption(new DiningPlace("Santorini Coffee", "Av. Manuel da Maia 19 a", customBitMapper(R.drawable.santorini), defaultSchedule, "Alameda", 38.735667, -9.136884));
        addDiningOption(new DiningPlace("Kokoro Ramen Bar", "Av. Rovisco Pais 30A", customBitMapper(R.drawable.ramen), defaultSchedule, "Alameda", 38.735341, -9.137885));


    }

}

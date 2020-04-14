package pt.ulisboa.tecnico.cmov.foodist;

import android.app.Application;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GlobalState extends Application {

    private String username;
    private String password;
    private Bitmap profilePicture;
    private String[] campuses;
    private boolean loggedIn;
    private String[] categories;
    private Map<String, ArrayList<DiningOption>> diningOptions;
    private int actualCategory;

    public GlobalState(){
        this.categories = new String[] {"Student", "Researcher", "Professor", "Staff", "General Public"};
        this.actualCategory = 0;
        this.loggedIn = false;

        // TO DO fetch everything below from MySQLite database:
        this.diningOptions = new HashMap<>();
        this.diningOptions.put("Alameda", new ArrayList<DiningOption>());
        this.diningOptions.put("Taguspark", new ArrayList<DiningOption>());
        this.campuses = new String[] {"Alameda", "Taguspark"};
    }

    public void login(String username, String password){
        // TO DO authentication
        this.username = username;
        this.password = password;
        this.loggedIn = true;
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

    public DiningOption getDiningOption(String campus, String diningOptionName){

        for (DiningOption diningOption: this.diningOptions.get(campus)) {
            if(diningOption.getName().equals(diningOptionName)){
                return diningOption;
            }
        }

        return null;
    }

    public int getDiningOptionIndex(String campus, String diningOptionName){

        int index = 0;

        for (DiningOption diningOption: this.diningOptions.get(campus)) {
            if(diningOption.getName().equals(diningOptionName)){
                return index;
            }
            index++;
        }

        return index;
    }

    public Dish getDish(String campus, String diningOptionName, String dishName){

        for (DiningOption diningOption: this.diningOptions.get(campus)) {
            if(diningOption.getName().equals(diningOptionName)){
                for (Dish dish: diningOption.getDishes()) {
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

        for (DiningOption diningOption: this.diningOptions.get(campus)) {
            if(diningOption.getName().equals(diningOptionName)){
                for (Dish dish: diningOption.getDishes()) {
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

        for(DiningOption diningOption: this.diningOptions.get(campus)) {
            result[index] = diningOption.getName();
            index++;
        }

        return result;
    }

    public String[] getDishNames(DiningOption diningOption){

        String[] result = new String[diningOption.getDishes().size()];
        int index = 0;

        for(Dish dish: diningOption.getDishes()) {
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

    public ArrayList<DiningOption> getDiningOptions(String campus) { return this.diningOptions.get(campus); }

    public DishImage getDishImage(String campus, String diningOptionName, String dishName, int imageId){

        Dish dish = getDish(campus, diningOptionName, dishName);
        return dish.getDishImage(imageId);
    }

    public Bitmap getProfilePicture() { return this.profilePicture; }

    public String[] getCampuses(){ return this.campuses; }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void addDiningOption(DiningOption diningOption){
        this.diningOptions.get(diningOption.getCampus()).add(diningOption);
    }

    public boolean isLoggedIn(){
        return this.loggedIn;
    }

    public void populateForTest(){

        //FOR TEST PURPOSES:

        String[] schedule = new String[]{"8:00-20:00", "10:00-16:00", "10:00-10:01", "16:00-19:00", "00:00-23:59"};

        DiningOption copacabana = new DiningOption("Copacabana", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule, "Alameda");

        copacabana.addDish(new Dish("Ensopado de Tetas", "20 paus", 1, customBitMapper(getResources().getIdentifier("plate1", "drawable", getPackageName())),"Bras"));
        copacabana.addDish(new Dish("Gaijas", "523 paus", 1, customBitMapper(getResources().getIdentifier("plate2", "drawable", getPackageName())), getUsername()));

        Dish ensopadoDeTetas = copacabana.getDish("Ensopado de Tetas");
        ensopadoDeTetas.addImage("Bras", customBitMapper(getResources().getIdentifier("plate1", "drawable", getPackageName())));
        //ensopadoDeTetas.addImage(getResources().getIdentifier("plate2", "drawable", getPackageName()), "Tetona", Bitmapper(getResources().getIdentifier("plate1", "drawable", getPackageName())));
        //ensopadoDeTetas.addImage(getResources().getIdentifier("plate3", "drawable", getPackageName()), "Bana", Bitmapper(getResources().getIdentifier("plate1", "drawable", getPackageName())));
        //ensopadoDeTetas.addImage(getResources().getIdentifier("plate4", "drawable", getPackageName()), "Zezão", Bitmapper(getResources().getIdentifier("plate1", "drawable", getPackageName())));

        addDiningOption(copacabana);
        addDiningOption(new DiningOption("Jucaca", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        addDiningOption(new DiningOption("Garfunkle", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        addDiningOption(new DiningOption("Kutxarra", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        addDiningOption(new DiningOption("Katuqui", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        addDiningOption(new DiningOption("Merekete", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        addDiningOption(new DiningOption("Kunami", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        addDiningOption(new DiningOption("Konami", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        addDiningOption(new DiningOption("Santos G", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule, "Taguspark"));
        addDiningOption(new DiningOption("Restaurante do José Brás", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule, "Taguspark"));
        addDiningOption(new DiningOption("Punanirolls", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule, "Taguspark"));
        addDiningOption(new DiningOption("Sexappeal Bar", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule, "Taguspark"));
    }

    public Bitmap customBitMapper(int imageId) {

        try {
            return BitmapFactory.decodeResource(getResources(), imageId);

        } catch (Exception e) {
            return null;

        }
    }

}

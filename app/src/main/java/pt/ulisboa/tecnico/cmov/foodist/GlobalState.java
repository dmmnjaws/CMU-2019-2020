package pt.ulisboa.tecnico.cmov.foodist;

import android.app.Application;

import java.util.ArrayList;

public class GlobalState extends Application {

    private String username;
    private String password;
    private boolean loggedIn;
    private String[] categories;
    private int actualCategory;
    private ArrayList<DiningOption> diningOptions;

    public GlobalState(){
        this.categories = new String[] {"Student", "Researcher", "Professor", "Staff", "General Public"};
        this.actualCategory = 0;
        this.loggedIn = false;

        // TO DO fetch everything below from MySQLite database:
        this.diningOptions = new ArrayList<>();
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

    public DiningOption getDiningOption(String diningOptionName){

        for (DiningOption diningOption: this.diningOptions) {
            if(diningOption.getName().equals(diningOptionName)){
                return diningOption;
            }
        }

        return null;
    }

    public int getDiningOptionIndex(String diningOptionName){

        int index = 0;

        for (DiningOption diningOption: this.diningOptions) {
            if(diningOption.getName().equals(diningOptionName)){
                return index;
            }
            index++;
        }

        return index;
    }

    public Dish getDish(String diningOptionName, String dishName){

        for (DiningOption diningOption: this.diningOptions) {
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

    public int getDishIndex(String diningOptionName, String dishName){

        int index = 0;

        for (DiningOption diningOption: this.diningOptions) {
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

    public String[] getDiningOptionNames(){

        String[] result = new String[diningOptions.size()];
        int index = 0;

        for(DiningOption diningOption: this.diningOptions) {
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

    public ArrayList<DiningOption> getDiningOptions() { return this.diningOptions; }

    public DishImage getDishImage(String diningOptionName, String dishName, int imageId){

        Dish dish = getDish(diningOptionName, dishName);
        return dish.getDishImage(imageId);
    }

    public boolean isLoggedIn(){
        return this.loggedIn;
    }

    public void populateForTest(){

        //FOR TEST PURPOSES:

        String[] schedule = new String[]{"8:00-20:00", "10:00-16:00", "10:00-10:01", "16:00-19:00", "00:00-23:59"};

        DiningOption copacabana = new DiningOption("Copacabana", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule);

        copacabana.addDish(new Dish("Ensopado de Tetas", "20 paus", 1, R.drawable.ic_options_threedots_background,"Bras"));
        copacabana.addDish(new Dish("Gaijas", "523 paus", 1, R.drawable.ic_options_threedots_background, getUsername()));

        Dish ensopadoDeTetas = copacabana.getDish("Ensopado de Tetas");
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate1", "drawable", getPackageName()), "Bras");
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate2", "drawable", getPackageName()), "Tetona");
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate3", "drawable", getPackageName()), "Bana");
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate4", "drawable", getPackageName()), "Zezão");
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate5", "drawable", getPackageName()), "Santos G");
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate3", "drawable", getPackageName()), "Coin");
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate1", "drawable", getPackageName()), "Penilento");
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate4", "drawable", getPackageName()), getUsername());
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate5", "drawable", getPackageName()), "Bras");
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate2", "drawable", getPackageName()), getUsername());
        ensopadoDeTetas.addImage(getResources().getIdentifier("plate3", "drawable", getPackageName()), "Bras");

        diningOptions.add(copacabana);
        diningOptions.add(new DiningOption("Jucaca", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Garfunkle", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Kutxarra", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Katuqui", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Merekete", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Kunami", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Konami", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Santos G", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Restaurante do José Brás", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Punanirolls", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule));
        diningOptions.add(new DiningOption("Sexappeal Bar", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule));
    }
}

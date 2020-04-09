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
                index++;
            }
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
                        index++;
                    }
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

    public boolean isLoggedIn(){
        return this.loggedIn;
    }

}

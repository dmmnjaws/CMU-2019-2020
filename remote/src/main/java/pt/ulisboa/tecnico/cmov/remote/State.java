package pt.ulisboa.tecnico.cmov.remote;

import java.util.ArrayList;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.library.DiningPlace;
import pt.ulisboa.tecnico.cmov.library.Dish;

public class State {

    private Map<String, ArrayList<DiningPlace>> diningOptions;

    public String Hello(){
        return "hello";
    }

    public String getDishName(Dish dish){
        return dish.getName();
    }

}

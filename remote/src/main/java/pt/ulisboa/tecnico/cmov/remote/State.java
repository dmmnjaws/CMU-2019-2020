package pt.ulisboa.tecnico.cmov.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.library.DiningPlace;
import pt.ulisboa.tecnico.cmov.library.Dish;

public class State {

    private Map<String, ArrayList<DiningPlace>> diningOptions;

    public State(){
        populateForTest();
    }

    public void populateForTest(){

        //FOR TEST PURPOSES:

        String[] schedule = new String[]{"8:00-20:00", "10:00-16:00", "10:00-10:01", "16:00-19:00", "00:00-23:59"};

        DiningPlace copacabana = new DiningPlace("Copacabana", "Rua da Joaquina", customBitMapper("remote/images/sala.jpg"), schedule, "Alameda");

        //copacabana.addDish(new Dish("Ensopado de Tetas", "20 paus", 1, customBitMapper("src/main/res/drawable/sala.jpg"),"Bras"));
        //copacabana.addDish(new Dish("Gaijas", "523 paus", 1, customBitMapper("src/main/res/drawable/sala.jpg"), "Antonio"));

        //Dish ensopadoDeTetas = copacabana.getDish("Ensopado de Tetas");
        //ensopadoDeTetas.addImage("Bras", customBitMapper("src/main/res/drawable/sala.jpg"));
        //ensopadoDeTetas.addImage(getResources().getIdentifier("plate2", "drawable", getPackageName()), "Tetona", Bitmapper(getResources().getIdentifier("plate1", "drawable", getPackageName())));
        //ensopadoDeTetas.addImage(getResources().getIdentifier("plate3", "drawable", getPackageName()), "Bana", Bitmapper(getResources().getIdentifier("plate1", "drawable", getPackageName())));
        //ensopadoDeTetas.addImage(getResources().getIdentifier("plate4", "drawable", getPackageName()), "Zezão", Bitmapper(getResources().getIdentifier("plate1", "drawable", getPackageName())));

        //addDiningOption(copacabana);
        //addDiningOption(new DiningPlace("Jucaca", "Rua da Maria Coxa", customBitMapper("src/main/res/drawable/sala.jpg"), schedule, "Taguspark"));
        //addDiningOption(new DiningPlace("Garfunkle", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        //addDiningOption(new DiningPlace("Kutxarra", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        //addDiningOption(new DiningPlace("Katuqui", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        //addDiningOption(new DiningPlace("Merekete", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        //addDiningOption(new DiningPlace("Kunami", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        //addDiningOption(new DiningPlace("Konami", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule, "Alameda"));
        //addDiningOption(new DiningPlace("Santos G", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule, "Taguspark"));
        //addDiningOption(new DiningPlace("Restaurante do José Brás", "Rua da Joaquina", R.drawable.ic_options_threedots_background, schedule, "Taguspark"));
        //addDiningOption(new DiningPlace("Punanirolls", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background, schedule, "Taguspark"));
        //addDiningOption(new DiningPlace("Sexappeal Bar", "Avenida Gay", R.drawable.ic_options_threedots_background, schedule, "Taguspark"));

    }

    public void addDiningOption(DiningPlace diningPlace){
        this.diningOptions.get(diningPlace.getCampus()).add(diningPlace);
    }

    public Map<String, ArrayList<DiningPlace>> getDiningOptions(){ return this.diningOptions; }

    public Bitmap customBitMapper(String path) {

        try {

            File file = new File (System.getProperty("user.dir") + "/" + path);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            System.out.println("ola");
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

}
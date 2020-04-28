package pt.ulisboa.tecnico.cmov.foodist;

import android.app.Application;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.IBinder;
import android.os.Messenger;

import android.widget.Toast;

import java.lang.Math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;

import pt.ulisboa.tecnico.cmov.foodist.asynctasks.ClientAuthenticator;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.CreateAccount;
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
    private Map<String,String> campusCoordinates;
    private boolean loggedIn;
    private String[] categories;
    private Map<String, ArrayList<DiningPlace>> diningOptions;
    private int actualCategory;
    private String userCoordinates;

    private Map<String, Boolean> preferences;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private boolean mBound = false;
    private SimWifiP2pBroadcastReceiver mReceiver;
    private String lastKnownBeacon;

    public GlobalState(){
        this.categories = new String[] {"Student", "Researcher", "Professor", "Staff", "General Public"};
        this.actualCategory = 0;
        this.loggedIn = false;

        this.preferences = new HashMap<>();
        this.preferences.put("Vegetarian", true);
        this.preferences.put("Vegan", true);
        this.preferences.put("Fish", true);
        this.preferences.put("Meat", true);
    }

    public void login(String username, String password) {
        this.username = username;
        this.password = password;

        ClientAuthenticator clientAuthenticator = new ClientAuthenticator(this);

        try{
            this.loggedIn = (boolean) clientAuthenticator.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(this.diningOptions == null) {
            populate();

            StateLoader stateLoader = new StateLoader(this);
            stateLoader.execute();
        }

        prepareWiFiDirect();

    }

    public void logWithoutAccount(){

        if(this.diningOptions == null) {
            populate();

            StateLoader stateLoader = new StateLoader(this);
            stateLoader.execute();
        }

        prepareWiFiDirect();
    }

    public synchronized void setState(ArrayList<DishesView> dishesViews){
        if (dishesViews != null){
            for(DishesView dishesView: dishesViews){
                DiningPlace diningPlace = getDiningOption(dishesView.getCampus(), dishesView.getDiningPlace());
                diningPlace.setDishes(dishesView.getDishes());
                diningPlace.setQueueTime(dishesView.getQueueTime());
            }
        }
    }

    public boolean createAccount(String username, String password){
        CreateAccount createAccount = new CreateAccount(username, password);
        boolean isCreated = false;

        try{
             isCreated = (boolean) createAccount.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isCreated){
            this.username = username;
            this.password = password;
            this.loggedIn = true;
            this.preferences.put("Vegetarian", true);
            this.preferences.put("Vegan", true);
            this.preferences.put("Fish", true);
            this.preferences.put("Meat", true);

        } else {
            Toast.makeText(getApplicationContext(), "That username is already taken...", Toast.LENGTH_LONG).show();
        }

        return isCreated;
    }

    public void setUsername(String username) { this.username = username; }

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

    public void setUserCoordinates(String location){
        System.out.println(location);
        this.userCoordinates = location;
    }

    public String getUserCoordinates(){
        return this.userCoordinates;
    }

    public int getNearestCampus(){

        while(userCoordinates == null){}

        double userLat = Double.parseDouble(this.userCoordinates.split(",")[0]);
        double userLong = Double.parseDouble(this.userCoordinates.split(",")[1]);

        double alamedaLat = Double.parseDouble(this.campusCoordinates.get("Alameda").split(",")[0]);
        double alamedaLong = Double.parseDouble(this.campusCoordinates.get("Alameda").split(",")[1]);

        double tagusLat = Double.parseDouble(this.campusCoordinates.get("Taguspark").split(",")[0]);
        double tagusLong = Double.parseDouble(this.campusCoordinates.get("Taguspark").split(",")[1]);

        double distAlameda = distance(userLat, alamedaLat, userLong, alamedaLong);
        double distTagus = distance(userLat, tagusLat, userLong, tagusLong);

        if(distAlameda >= 8 && distTagus >= 8){
            return 2;
        }

        if(distAlameda > distTagus){
            return 1;
        }else{
            return 0;
        }

    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371;

        Double latDistance = (lat2-lat1) * Math.PI / 180;
        Double lonDistance = (lon2-lon1) * Math.PI / 180;
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    public void prepareWiFiDirect(){

        // LIKE IN LAB 4: register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        setmReceiver(new SimWifiP2pBroadcastReceiver(this));
        registerReceiver(mReceiver, filter);

        // LIKE IN LAB 4: Bind WiFi Direct
        Intent intent = new Intent(getBaseContext(), SimWifiP2pService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;

    }

    public SimWifiP2pBroadcastReceiver getMReceiver(){ return this.mReceiver; }

    public SimWifiP2pManager getMManager(){ return this.mManager; }

    public SimWifiP2pManager.Channel getMChannel(){ return this.mChannel; }

    public ServiceConnection getMConnection(){ return this.mConnection; }

    public boolean getMbound(){ return this.mBound; }

    public boolean setMBound(boolean mBound){ return this.mBound = mBound; }

    public void setmReceiver(SimWifiP2pBroadcastReceiver mReceiver){ this.mReceiver = mReceiver;}

    public String getLastKnownBeacon(){ return this.lastKnownBeacon; }

    public void setLastKnownBeacon(String lastKnownBeacon){ this.lastKnownBeacon = lastKnownBeacon; }

    private ServiceConnection mConnection = new ServiceConnection() {
        // LIKE IN LAB 4: callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mManager = new SimWifiP2pManager(new Messenger(service));
            mChannel = mManager.initialize(getApplicationContext(), getMainLooper(), null);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };

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

    public int getDishBasedOnPreferenceIndex(String campus, String diningOptionName, String dishName){
        int index = 0;

        for (DiningPlace diningPlace : this.diningOptions.get(campus)) {
            if(diningPlace.getName().equals(diningOptionName)){
                for (Dish dish: diningPlace.getDishesBasedOnPreference(this.preferences)) {
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

    public Map<String, Boolean> getPreferences() {
        return this.preferences;
    }

    public void setPreferences(Map<String, Boolean> preferences){
        this.preferences = preferences;
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

    public void setLoggedIn(boolean loggedIn){
        this.loggedIn = loggedIn;
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

        this.campusCoordinates = new HashMap<>();
        this.campusCoordinates.put("Alameda","38.736796,-9.138670");
        this.campusCoordinates.put("Taguspark","38.737333,-9.302568");

    }

}

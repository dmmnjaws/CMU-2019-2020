package pt.ulisboa.tecnico.cmov.foodist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.StateLoader;
import pt.ulisboa.tecnico.cmov.library.DiningPlace;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.foodist.adapters.DishAdapter;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class DiningPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {

    private DiningPlace diningPlace;
    private GlobalState globalState;
    private String campus;
    private GoogleMap mMap;
    private float inRating;

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_place);
        this.setTitle("FoodIST - Dining Place");
        this.globalState = (GlobalState) getApplication();
        this.campus = (String) getIntent().getSerializableExtra("campus");

        ListView listOfDishes = (ListView) findViewById(R.id.listOfDishes);

        listOfDishes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Dish item = (Dish) parent.getItemAtPosition(position);
                Intent intent = new Intent(DiningPlaceActivity.this, DishActivity.class);
                intent.putExtra("diningOptionName", item.getDiningPlace());
                intent.putExtra("dishName", item.getName());
                intent.putExtra("campus", diningPlace.getCampus());
                startActivity(intent);
            }

        });

        Spinner spinner = findViewById(R.id.chooseDiningPlaceSpinner);

        String[] diningOptionNames = this.globalState.getDiningOptionNames(this.campus);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diningOptionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final String diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");

        spinner.setSelection(this.globalState.getDiningOptionIndex(this.campus, diningOptionName));
        spinner.setOnItemSelectedListener(this);


        View buttonInflater = (View) getLayoutInflater().inflate(R.layout.upload_button,null);
        Button addDishButton = (Button) buttonInflater.findViewById(R.id.upload);
        addDishButton.setText("Upload Dish");
        listOfDishes.addFooterView(buttonInflater);

        addDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent intent = new Intent(DiningPlaceActivity.this, DishUploadActivity.class);
                intent.putExtra("diningOptionName", diningPlace.getName());
                intent.putExtra("campus", diningPlace.getCampus());
                startActivity(intent);
            }
        });

        authenticateCheck(addDishButton);

    }

    @Override
    public void onPause() {

        super.onPause();

    }

    @Override
    protected void onResume () {
        super.onResume();

        if (this.globalState.getShouldSeeWarning()){

            new AlertDialog.Builder(this)
                    .setTitle("Your preferences...")
                    .setMessage("Some dishes are hidden according to the preferences you selected in your profile.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();

            this.globalState.changeShouldSeeWarning(false);

        }

        String diningOptionName = ((Spinner) findViewById(R.id.chooseDiningPlaceSpinner)).getSelectedItem().toString();

        populateActivity(diningOptionName);
        StateLoader stateLoader = new StateLoader(this.globalState);
        stateLoader.execute();

    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DiningPlaceActivity.this, UserProfileActivity.class);
        startActivity(intent);

    }

    public void populateActivity (String diningOptionName){

        this.diningPlace = this.globalState.getDiningOption(this.campus, diningOptionName);

        ListView listOfDishes = (ListView) findViewById(R.id.listOfDishes);

        ((TextView) findViewById(R.id.diningOptionSchedule)).setText("Schedule: " + this.diningPlace.getSchedule(this.globalState.getActualCategoryIndex()));
        ((TextView) findViewById(R.id.diningOptionQueueTime)).setText("Average queue time: " + this.diningPlace.getQueueTime());

        ArrayList<Dish> dishes = this.diningPlace.getDishesBasedOnPreference(this.globalState.getPreferences());
        DishAdapter dishAdapter = new DishAdapter(getApplicationContext(), R.layout.list_row_dish, dishes);
        listOfDishes.setAdapter(dishAdapter);
        dishAdapter.notifyDataSetChanged();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.inRating = this.diningPlace.getRating();
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(this.inRating);

        barChart = (BarChart) findViewById(R.id.bargraph);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int numOfOnes = 0;
        int numOfTwos = 0;
        int numOfThrees = 0;
        int numOfFours = 0;
        int numOfFives = 0;
        for(Dish dish: diningPlace.getDishes()) {
            if (dish.getRating() == 0.5 || dish.getRating() == 1) {
                numOfOnes++;
            }
            if (dish.getRating() == 1.5 || dish.getRating() == 2) {
                numOfTwos++;
            }
            if (dish.getRating() == 2.5 || dish.getRating() == 3) {
                numOfThrees++;
            }
            if (dish.getRating() == 3.5 || dish.getRating() == 4) {
                numOfFours++;
            }
            if (dish.getRating() == 4.5 || dish.getRating() == 5) {
                numOfFives++;
            }
        }

        barEntries.add(new BarEntry(1f, numOfOnes));
        barEntries.add(new BarEntry(2f, numOfTwos));
        barEntries.add(new BarEntry(3f, numOfThrees));
        barEntries.add(new BarEntry(4f, numOfFours));
        barEntries.add(new BarEntry(5f, numOfFives));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Ratings");
        barDataSet.setColors(new int[] {Color.LTGRAY, Color.LTGRAY, Color.LTGRAY, Color.LTGRAY, Color.LTGRAY});

        BarData theData = new BarData(barDataSet);
        theData.setBarWidth(0.9f);
        barChart.setData(theData);
        barChart.setFitBars(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getBarData().setValueTextColor(Color.WHITE);
        barChart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        populateActivity(parent.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void authenticateCheck(Button addDishButton){
        if (!this.globalState.isLoggedIn()){
            ((Button) findViewById(R.id.optionsButton)).setEnabled(false);
            addDishButton.setEnabled(false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mMap = googleMap;
        googleMap.clear();
        LatLng diningPlaceCoordinates = new LatLng(this.diningPlace.getCoordinates()[0], this.diningPlace.getCoordinates()[1]);
        Marker thisMarker = this.mMap.addMarker(new MarkerOptions().position(diningPlaceCoordinates).title(this.diningPlace.getName()));

        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(diningPlaceCoordinates));
        thisMarker.showInfoWindow();
        this.mMap.setMinZoomPreference(16);
    }
}

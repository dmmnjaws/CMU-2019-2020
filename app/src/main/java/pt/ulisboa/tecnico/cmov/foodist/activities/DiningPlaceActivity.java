package pt.ulisboa.tecnico.cmov.foodist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.ArrayList;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.library.DiningPlace;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.foodist.adapters.DishAdapter;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class DiningPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SimWifiP2pManager.PeerListListener {

    private DiningPlace diningPlace;
    private GlobalState globalState;
    private String campus;

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

        // LIKE IN LAB 4: Request peers in range
        if (this.globalState.getMbound()) {
            this.globalState.getMManager().requestPeers(this.globalState.getMChannel(), DiningPlaceActivity.this);
        } else {
            Toast.makeText(getBaseContext(), "Could not calculate queue time for this restaurant.", Toast.LENGTH_SHORT).show();
        }

        authenticateCheck(addDishButton);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume () {
        super.onResume();

        String diningOptionName = ((Spinner) findViewById(R.id.chooseDiningPlaceSpinner)).getSelectedItem().toString();

        populateActivity(diningOptionName);

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

        ArrayList<Dish> dishes = this.diningPlace.getDishes();
        DishAdapter dishAdapter = new DishAdapter(getApplicationContext(), R.layout.list_row_dish, dishes);
        listOfDishes.setAdapter(dishAdapter);
    }

    public void checkLocationOnClick(View view){
        Intent intent = new Intent(DiningPlaceActivity.this, DiningPlaceMapActivity.class);
        intent.putExtra("coordinates", this.diningPlace.getCoordinates());
        intent.putExtra( "diningOptionName", this.diningPlace.getName());
        intent.putExtra("campus", this.diningPlace.getCampus());
        startActivity(intent);

        //ALTERNATIVE (LIGHTER):
        //Skip the entirety of DiningPlaceMapActivity and go straight to directions:

        /*
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", this.diningPlace.getCoordinates()[0], this.diningPlace.getCoordinates()[1], this.diningPlace.getName());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
         */
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        populateActivity(parent.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        // LIKE IN LAB 4: when peers detected...

        int queueTime = 0;

        queueTime = peers.getDeviceList().size();

        this.diningPlace.setQueueTime(queueTime + " minutes");
        ((TextView) findViewById(R.id.diningOptionQueueTime)).setText("Average queue time: " + this.diningPlace.getQueueTime());

    }

    public void authenticateCheck(Button addDishButton){
        if (!this.globalState.isLoggedIn()){
            ((Button) findViewById(R.id.optionsButton)).setEnabled(false);
            addDishButton.setEnabled(false);
        }
    }
}

package pt.ulisboa.tecnico.cmov.foodist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.StateLoader;
import pt.ulisboa.tecnico.cmov.library.DiningPlace;
import pt.ulisboa.tecnico.cmov.foodist.adapters.DiningOptionAdapter;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class DiningOptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GlobalState globalState;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_options);

        this.setTitle("FoodIST - Campus");
        this.globalState = (GlobalState) getApplication();

        this.spinner = findViewById(R.id.chooseCampusSpinner);

        String[] campuses = this.globalState.getCampuses();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, campuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        ListView listOfDiningPlaces = (ListView) findViewById(R.id.listOfDiningPlaces);
        listOfDiningPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DiningPlace item = (DiningPlace) parent.getItemAtPosition(position);
                Intent intent = new Intent(DiningOptionsActivity.this, DiningPlaceActivity.class);
                intent.putExtra("diningOptionName", item.getName());
                intent.putExtra("campus", item.getCampus());
                startActivity(intent);
            }

        });

        authenticateCheck();

        closestCampusCheck();

    }

    @Override
    protected void onResume () {
        super.onResume();

        populateActivity(this.spinner.getSelectedItem().toString());
        StateLoader stateLoader = new StateLoader(this.globalState);
        stateLoader.execute();
    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DiningOptionsActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void populateActivity(String campus){

        ListView listOfDiningPlaces = (ListView) findViewById(R.id.listOfDiningPlaces);
        ArrayList<DiningPlace> diningPlaces = this.globalState.getDiningOptions(campus);

        DiningOptionAdapter diningOptionAdapter = new DiningOptionAdapter(getApplicationContext(), R.layout.list_row_dining_option, diningPlaces);
        listOfDiningPlaces.setAdapter(diningOptionAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        populateActivity(parent.getSelectedItem().toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void authenticateCheck(){
        if (!this.globalState.isLoggedIn()){

            new AlertDialog.Builder(this)
                    .setTitle("You're still not registered!?")
                    .setMessage("The good news: you're still able to see what's up with the restaurants around you. The bad news: you won't be able to share your dishes and opinions without an account. So... what are you waiting for?")
                    .setPositiveButton("Create Account", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(DiningOptionsActivity.this, CreateAccountActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();

            ((Button) findViewById(R.id.optionsButton)).setEnabled(false);
        }
    }

    public void closestCampusCheck(){

        int closestCampus = this.globalState.getNearestCampus();

        if (closestCampus == 2){

            new AlertDialog.Builder(this)
                    .setTitle("Where are you?")
                    .setMessage("We could not detect you to be close to a campus. Which campus would you like to see?")
                    .setNeutralButton("Alameda", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            spinner.setSelection(0);
                        }
                    })
                    .setPositiveButton("Taguspark", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            spinner.setSelection(1);
                        }
                    })
                    .show();

        } else {

            this.spinner.setSelection(closestCampus);

        }
    }
}

package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class DiningOptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_options);
        this.setTitle("FoodIST - Dining Options");

        this.globalState = (GlobalState) getApplication();


        Spinner spinner = findViewById(R.id.chooseCampusSpinner);

        String[] campuses = this.globalState.getCampuses();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, campuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);

        this.globalState.populateForTest();

        ListView listOfDiningPlaces = (ListView) findViewById(R.id.listOfDiningPlaces);
        listOfDiningPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DiningOption item = (DiningOption) parent.getItemAtPosition(position);
                Intent intent = new Intent(DiningOptionsActivity.this, DiningPlaceActivity.class);
                intent.putExtra("diningOptionName", item.getName());
                intent.putExtra("campus", item.getCampus());
                startActivity(intent);
            }

        });

        populateActivity(spinner.getSelectedItem().toString());

    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DiningOptionsActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void populateActivity(String campus){
        ListView listOfDiningPlaces = (ListView) findViewById(R.id.listOfDiningPlaces);
        ArrayList<DiningOption> diningOptions = this.globalState.getDiningOptions(campus);

        DiningOptionAdapter diningOptionAdapter = new DiningOptionAdapter(getApplicationContext(), R.layout.list_row_dining_option, diningOptions);
        listOfDiningPlaces.setAdapter(diningOptionAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        populateActivity(parent.getSelectedItem().toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

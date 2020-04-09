package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DiningPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DiningOption diningOption;
    private GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_place);
        this.setTitle("FoodIST - Dining Place");
        this.globalState = (GlobalState) getApplication();

        ListView listOfDishes = (ListView) findViewById(R.id.listOfDishes);

        listOfDishes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Dish item = (Dish) parent.getItemAtPosition(position);
                Intent intent = new Intent(DiningPlaceActivity.this, DishActivity.class);
                intent.putExtra("diningOptionName", item.getDiningPlace());
                intent.putExtra("dishName", item.getName());
                startActivity(intent);
            }

        });

        Spinner spinner = findViewById(R.id.chooseDiningPlaceSpinner);

        String[] diningOptionNames = this.globalState.getDiningOptionNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diningOptionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");

        spinner.setSelection(this.globalState.getDiningOptionIndex(diningOptionName));
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    protected void onResume () {
        super.onResume();

        String diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");
        this.setTitle("FoodIST - " + diningOptionName);

        populateActivity(diningOptionName);

    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DiningPlaceActivity.this, UserProfileActivity.class);
        startActivity(intent);

    }

    public void populateActivity (String diningOptionName){

        this.diningOption = this.globalState.getDiningOption(diningOptionName);

        ListView listOfDishes = (ListView) findViewById(R.id.listOfDishes);

        ((TextView) findViewById(R.id.diningOptionSchedule)).setText("Schedule: " + this.diningOption.getSchedule(this.globalState.getActualCategoryIndex()));
        ((TextView) findViewById(R.id.diningOptionQueueTime)).setText("Average queue time: " + this.diningOption.getQueueTime());

        ArrayList<Dish> dishes = this.diningOption.getDishes();
        DishAdapter dishAdapter = new DishAdapter(getApplicationContext(), R.layout.list_row_dish, dishes);
        listOfDishes.setAdapter(dishAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

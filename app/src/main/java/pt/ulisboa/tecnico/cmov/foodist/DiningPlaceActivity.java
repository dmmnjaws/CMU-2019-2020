package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class DiningPlaceActivity extends AppCompatActivity {

    DiningOption diningOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_place);
        this.setTitle("FoodIST - Dining Place");

    }

    @Override
    protected void onResume () {
        super.onResume();

        DiningOption diningOption = (DiningOption) getIntent().getSerializableExtra("diningOption");
        this.setTitle("FoodIST - " + diningOption.getName());

        ListView listOfDishes = (ListView) findViewById(R.id.listOfDishes);
        ArrayList<Dish> dishes = diningOption.getDishes();
        DishAdapter dishAdapter = new DishAdapter(getApplicationContext(), R.layout.list_row_dish, dishes);
        listOfDishes.setAdapter(dishAdapter);

    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DiningPlaceActivity.this, UserProfileActivity.class);
        startActivity(intent);

    }
}

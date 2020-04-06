package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DiningPlaceActivity extends AppCompatActivity {

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
                intent.putExtra("dish", item);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onResume () {
        super.onResume();

        this.diningOption = (DiningOption) getIntent().getSerializableExtra("diningOption");
        this.setTitle("FoodIST - " + this.diningOption.getName());

        ArrayList<DiningOption> list = this.globalState.getDiningOptions();
        for (DiningOption x: list) {
            if(x.getName().equals(this.diningOption.getName())){
                this.diningOption = x;
            }
        }

        ListView listOfDishes = (ListView) findViewById(R.id.listOfDishes);

        ((TextView) findViewById(R.id.diningOptionName)).setText(this.diningOption.getName());
        ((TextView) findViewById(R.id.diningOptionAddress)).setText(this.diningOption.getAddress());

        ArrayList<Dish> dishes = this.diningOption.getDishes();
        DishAdapter dishAdapter = new DishAdapter(getApplicationContext(), R.layout.list_row_dish, dishes);
        listOfDishes.setAdapter(dishAdapter);

    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DiningPlaceActivity.this, UserProfileActivity.class);
        startActivity(intent);

    }

}

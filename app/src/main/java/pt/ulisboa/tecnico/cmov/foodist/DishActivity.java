package pt.ulisboa.tecnico.cmov.foodist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DishActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GlobalState globalState;
    private Dish dish;
    private String diningOptionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        this.globalState = (GlobalState) getApplication();

        ListView listOfDishImages = (ListView) findViewById(R.id.listOfDishImages);

        listOfDishImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DishImage item = (DishImage) parent.getItemAtPosition(position);
                Intent intent = new Intent(DishActivity.this, DishPictureActivity.class);
                intent.putExtra("imageId", item.getImageId());
                intent.putExtra("dishName", item.getDishName());
                intent.putExtra("diningOptionName", item.getDiningPlace());
                startActivity(intent);

            }

        });

        Spinner spinner = findViewById(R.id.chooseDishSpinner);

        String diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");

        String[] dishNames = this.globalState.getDishNames(this.globalState.getDiningOption(diningOptionName));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dishNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String dishName = (String) getIntent().getSerializableExtra("dishName");

        spinner.setSelection(this.globalState.getDishIndex(diningOptionName, dishName));
        spinner.setOnItemSelectedListener(this);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                dish.addRating(globalState.getUsername(),rating);
            }
        });


    }

    @Override
    protected void onResume () {
        super.onResume();

        this.setTitle("FoodIST - " + "TO DO");

        this.diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");
        String dishName = (String) getIntent().getSerializableExtra("dishName");

        populateActivity(diningOptionName, dishName);

    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DishActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void populateActivity (String diningOptionName, String dishName){

        this.dish = this.globalState.getDish(diningOptionName, dishName);

        ListView listOfDishImages = (ListView) findViewById(R.id.listOfDishImages);

        ((TextView) findViewById(R.id.dishName)).setText(dishName);
        ((TextView) findViewById(R.id.dishCost)).setText(this.dish.getCost());
        ((RatingBar) findViewById(R.id.ratingBar)).setRating(this.dish.getUserRating(this.globalState.getUsername()));

        DishImageAdapter dishImageAdapter = new DishImageAdapter(getApplicationContext(), R.layout.list_item_dish_image, this.dish.getImages());
        listOfDishImages.setAdapter(dishImageAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        populateActivity(this.diningOptionName, parent.getSelectedItem().toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

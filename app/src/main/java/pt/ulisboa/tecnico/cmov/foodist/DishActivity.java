package pt.ulisboa.tecnico.cmov.foodist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class DishActivity extends AppCompatActivity {

    private GlobalState globalState;
    private Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        this.globalState = (GlobalState) getApplication();
        String diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");
        String dishName = (String) getIntent().getSerializableExtra("dishName");
        this.setTitle("FoodIST - " + dishName);

        this.dish = this.globalState.getDish(diningOptionName, dishName);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setRating(this.dish.getUserRating(this.globalState.getUsername()));
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

        ((TextView) findViewById(R.id.dishName)).setText(this.dish.getName());
        ((TextView) findViewById(R.id.dishCost)).setText(this.dish.getCost());
    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DishActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }
}

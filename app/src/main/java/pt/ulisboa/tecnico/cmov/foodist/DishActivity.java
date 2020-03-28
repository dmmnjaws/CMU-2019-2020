package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        this.setTitle("FoodIST - Dish"); //we shall then make this show the correct name of the dish
    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DishActivity.this, UserProfileActivity.class);
        startActivity(intent);

    }
}

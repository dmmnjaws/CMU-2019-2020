package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DishUploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_upload);
        this.setTitle("FoodIST - Share Dish");
    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DishUploadActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void shareButtonOnClick(View view) {

        //TO DO share dish (load into database)
    }

    public void dishPictureOnClick(View view) {

        // TO DO upload dish picture
    }
}

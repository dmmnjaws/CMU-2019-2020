package pt.ulisboa.tecnico.cmov.foodist.activities;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import pt.ulisboa.tecnico.cmov.foodist.Dish;
import pt.ulisboa.tecnico.cmov.foodist.DishImage;
import pt.ulisboa.tecnico.cmov.foodist.adapters.DishImageAdapter;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class DishActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int GALLERY_REQUEST_CODE = 100;
    private GlobalState globalState;
    private Dish dish;
    private String diningOptionName;
    private String campus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        this.globalState = (GlobalState) getApplication();

        this.setTitle("FoodIST - Dish");

        ListView listOfDishImages = (ListView) findViewById(R.id.listOfDishImages);

        View buttonInflater = (View) getLayoutInflater().inflate(R.layout.upload_button,null);
        ImageButton uploadButton = (ImageButton) buttonInflater.findViewById(R.id.upload);
        listOfDishImages.addHeaderView(buttonInflater);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });

        listOfDishImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DishImage item = (DishImage) parent.getItemAtPosition(position);
                Intent intent = new Intent(DishActivity.this, DishPictureActivity.class);
                intent.putExtra("imageId", item.getImageId());
                intent.putExtra("dishName", item.getDishName());
                intent.putExtra("diningOptionName", item.getDiningPlace());
                intent.putExtra("campus", campus);
                startActivity(intent);
            }

        });

        Spinner spinner = findViewById(R.id.chooseDishSpinner);

        this.diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");
        this.campus = (String) getIntent().getSerializableExtra("campus");

        String[] dishNames = this.globalState.getDishNames(this.globalState.getDiningOption(this.campus, this.diningOptionName));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dishNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String dishName = (String) getIntent().getSerializableExtra("dishName");

        spinner.setSelection(this.globalState.getDishIndex(this.campus, this.diningOptionName, dishName));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                this.dish.addImage(this.globalState.getUsername(),BitmapFactory.decodeStream(imageStream));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume () {
        super.onResume();

        this.diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");
        String dishName = ((Spinner) findViewById(R.id.chooseDishSpinner)).getSelectedItem().toString();

        populateActivity(this.campus, diningOptionName, dishName);

    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DishActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void populateActivity (String campus, String diningOptionName, String dishName){

        this.dish = this.globalState.getDish(campus, diningOptionName, dishName);

        ListView listOfDishImages = (ListView) findViewById(R.id.listOfDishImages);

        ((TextView) findViewById(R.id.dishName)).setText(dishName);
        ((TextView) findViewById(R.id.dishCost)).setText(this.dish.getCost());
        ((RatingBar) findViewById(R.id.ratingBar)).setRating(this.dish.getUserRating(this.globalState.getUsername()));

        DishImageAdapter dishImageAdapter = new DishImageAdapter(getApplicationContext(), R.layout.list_item_dish_image, this.dish.getImages());
        listOfDishImages.setAdapter(dishImageAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        populateActivity(this.campus, this.diningOptionName, parent.getSelectedItem().toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

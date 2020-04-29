package pt.ulisboa.tecnico.cmov.foodist.activities;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.foodist.asynctasks.AddDishImageRemotely;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.AddRatingRemotely;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.StateLoader;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.library.DishImage;
import pt.ulisboa.tecnico.cmov.foodist.adapters.DishImageAdapter;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;


public class DishActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int GALLERY_REQUEST_CODE = 100;
    private GlobalState globalState;
    private Dish dish;
    private String diningOptionName;
    private String campus;
    private RatingBar.OnRatingBarChangeListener listener;
    private float inRating;
    private float outRating;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        this.globalState = (GlobalState) getApplication();

        this.setTitle("FoodIST - Dish");

        ListView listOfDishImages = (ListView) findViewById(R.id.listOfDishImages);

        View buttonInflater = (View) getLayoutInflater().inflate(R.layout.upload_button,null);
        Button uploadButton = (Button) buttonInflater.findViewById(R.id.upload);
        uploadButton.setText("Upload Picture");
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

        String[] dishNames = this.globalState.getDishNamesByPreference(this.globalState.getDiningOption(this.campus, this.diningOptionName));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dishNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final String dishName = (String) getIntent().getSerializableExtra("dishName");

        spinner.setSelection(this.globalState.getDishBasedOnPreferenceIndex(this.campus, this.diningOptionName, dishName));
        spinner.setOnItemSelectedListener(this);

        this.listener = new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                outRating = rating;
            }
        };

        authenticateCheck(uploadButton);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);

                DishImage newDishImage = new DishImage(this.globalState.getUsername(),
                        BitmapFactory.decodeStream(imageStream), this.diningOptionName, this.dish.getName());

                this.globalState.addDishImage(this.dish, newDishImage);

                AddDishImageRemotely newImage = new AddDishImageRemotely(newDishImage);
                newImage.execute();

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

    @Override
    protected void onPause(){
        super.onPause();

        updateRating();

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

        this.inRating = this.dish.getUserRating(this.globalState.getUsername());
        this.outRating = this.dish.getUserRating(this.globalState.getUsername());
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(null);
        ratingBar.setRating(this.inRating);
        ratingBar.setOnRatingBarChangeListener(this.listener);


        Map<String, Boolean> categories = this.dish.getCategories();
        ((CheckBox) findViewById(R.id.vegetarianCheckBox)).setChecked(categories.get("Vegetarian"));
        ((CheckBox) findViewById(R.id.veganCheckBox)).setChecked(categories.get("Vegan"));
        ((CheckBox) findViewById(R.id.meatCheckBox)).setChecked(categories.get("Meat"));
        ((CheckBox) findViewById(R.id.fishCheckBox)).setChecked(categories.get("Fish"));

        DishImageAdapter dishImageAdapter = new DishImageAdapter(getApplicationContext(), R.layout.list_item_dish_image, this.dish.getImages());
        listOfDishImages.setAdapter(dishImageAdapter);

        barChart = (BarChart) findViewById(R.id.bargraph);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int numOfOnes = 0;
        int numOfTwos = 0;
        int numOfThrees = 0;
        int numOfFours = 0;
        int numOfFives = 0;
        for (Float rating : dish.getVoterRatings().values()) {
            if(rating == 0.5 || rating == 1){ numOfOnes++; }
            if(rating == 1.5 || rating == 2){ numOfTwos++; }
            if(rating == 2.5 || rating == 3){ numOfThrees++; }
            if(rating == 3.5 || rating == 4){ numOfFours++; }
            if(rating == 4.5 || rating == 5){ numOfFives++; }
        }

        barEntries.add(new BarEntry(1f, numOfOnes));
        barEntries.add(new BarEntry(2f, numOfTwos));
        barEntries.add(new BarEntry(3f, numOfThrees));
        barEntries.add(new BarEntry(4f, numOfFours));
        barEntries.add(new BarEntry(5f, numOfFives));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Ratings");
        barDataSet.setColors(new int[] {Color.LTGRAY, Color.LTGRAY, Color.LTGRAY, Color.LTGRAY, Color.LTGRAY});

        BarData theData = new BarData(barDataSet);
        theData.setBarWidth(0.9f);
        barChart.setData(theData);
        barChart.setFitBars(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getBarData().setValueTextColor(Color.WHITE);
        barChart.invalidate();



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        updateRating();
        populateActivity(this.campus, this.diningOptionName, parent.getSelectedItem().toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void authenticateCheck(Button addImageButton){
        if (!this.globalState.isLoggedIn()){
            ((Button) findViewById(R.id.optionsButton)).setEnabled(false);
            addImageButton.setEnabled(false);
            ((RatingBar) findViewById(R.id.ratingBar)).setEnabled(false);

        }
    }

    public void updateRating(){
        if (outRating != inRating){
            dish.addRating(globalState.getUsername(),outRating);
            AddRatingRemotely addRating = new AddRatingRemotely(diningOptionName,dish.getName(),globalState.getUsername(),outRating);
            addRating.execute();
        }
    }
}

package pt.ulisboa.tecnico.cmov.foodist.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import pt.ulisboa.tecnico.cmov.foodist.asynctasks.AddDishImageRemotely;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.AddDishRemotely;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.StateLoader;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;
import pt.ulisboa.tecnico.cmov.library.DishImage;

public class DishUploadActivity extends Activity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private String diningOptionName;
    private String campus;
    private GlobalState globalState;
    private Bitmap dishImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dish_upload);
        this.setTitle("FoodIST - Adding new dish");

        this.globalState = (GlobalState) getApplication();
        this.diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");
        this.campus = (String) getIntent().getSerializableExtra("campus");

    }

    @Override
    public void onResume(){
        super.onResume();

        if(dishImage != null) {
            ((ImageView) findViewById(R.id.dishPicture)).setImageBitmap(this.dishImage);
        }else{
            ((ImageView) findViewById(R.id.dishPicture)).setImageBitmap(this.globalState.customBitMapper(getResources().getIdentifier("uploadimage", "drawable", getPackageName())));
        }
    }

    public void addImageOnClick(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    public void addDishButtonOnClick(View view){

        String dishName = ((EditText) findViewById(R.id.insertDishName)).getText().toString();
        String dishPrice = ((EditText) findViewById(R.id.insertPrice)).getText().toString();
        float dishRating = ((RatingBar) findViewById(R.id.ratingBar)).getRating();

        boolean isVegetarian = ((CheckBox) findViewById(R.id.vegetarianCheckBox)).isChecked();
        boolean isVegan = ((CheckBox) findViewById(R.id.veganCheckBox)).isChecked();
        boolean isMeat = ((CheckBox) findViewById(R.id.meatCheckBox)).isChecked();
        boolean isFish = ((CheckBox) findViewById(R.id.fishCheckBox)).isChecked();

        if(dishName.equals("")) {
            Toast.makeText(getApplicationContext(), "You must indicate the name of the dish!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dishPrice.equals("")) {
            Toast.makeText(getApplicationContext(), "You must indicate the cost of the dish!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isVegetarian && !isVegan && !isFish && !isMeat){
            Toast.makeText(getApplicationContext(), "You must indicate the category of the dish!", Toast.LENGTH_SHORT).show();
            return;
        }

        Dish newDish = new Dish(dishName, dishPrice, dishRating, this.globalState.getUsername());
        Dish sendDish = new Dish(dishName, dishPrice, dishRating, this.globalState.getUsername());
        DishImage icon = null;
        byte[] imageBytes = null;
        if (this.dishImage != null){
            icon = new DishImage(this.globalState.getUsername(), this.dishImage, this.diningOptionName, dishName);
            newDish.addImage(icon);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            this.dishImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageBytes = stream.toByteArray();
            this.globalState.getCache().insertImageCache(icon, imageBytes);
        }

        newDish.setCategories(isVegetarian, isVegan, isMeat, isFish);
        sendDish.setCategories(isVegetarian, isVegan, isMeat, isFish);
        sendDish.setDiningPlace(this.diningOptionName);
        this.globalState.addDish(this.campus, this.diningOptionName, newDish);
        AddDishRemotely addDishRemotely = new AddDishRemotely(sendDish, icon, imageBytes);
        addDishRemotely.execute();

        finish();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                this.dishImage = BitmapFactory.decodeStream(imageStream);
                ((ImageView) findViewById(R.id.dishPicture)).setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void onCheckboxClicked(View view) {

        CheckBox isVegetarian = (CheckBox) findViewById(R.id.vegetarianCheckBox);
        CheckBox isVegan = (CheckBox) findViewById(R.id.veganCheckBox);
        CheckBox isMeat = (CheckBox) findViewById(R.id.meatCheckBox);
        CheckBox isFish = (CheckBox) findViewById(R.id.fishCheckBox);

        if (isVegetarian != view){
            isVegetarian.setChecked(false);
        }

        if (isVegan != view){
            isVegan.setChecked(false);
        }

        if (isMeat != view){
            isMeat.setChecked(false);
        }

        if (isFish != view){
            isFish.setChecked(false);
        }

    }
}

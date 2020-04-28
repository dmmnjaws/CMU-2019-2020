package pt.ulisboa.tecnico.cmov.foodist.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

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
        boolean isGlutenFree = ((CheckBox) findViewById(R.id.glutenFreeCheckBox)).isChecked();
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

        if(!isVegetarian && !isGlutenFree && !isFish && !isMeat){
            Toast.makeText(getApplicationContext(), "The dish must be of at least one category!", Toast.LENGTH_SHORT).show();
            return;
        }

        Dish newDish = new Dish(dishName, dishPrice, dishRating, this.globalState.getUsername());
        if (this.dishImage != null){
            newDish.addImage(new DishImage(this.globalState.getUsername(), this.dishImage, diningOptionName, dishName));
        }

        newDish.setCategories(isVegetarian, isGlutenFree, isMeat, isFish);
        this.globalState.addDish(this.campus, this.diningOptionName, newDish);

        AddDishRemotely addDishRemotely = new AddDishRemotely(newDish);
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

}

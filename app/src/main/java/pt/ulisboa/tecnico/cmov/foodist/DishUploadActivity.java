package pt.ulisboa.tecnico.cmov.foodist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class DishUploadActivity extends Activity {

    private static final int GALLERY_REQUEST_CODE = 100;
    private String diningOptionName;
    private GlobalState globalState;
    private Bitmap dishImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dish_upload);
        this.setTitle("FoodIST - Adding new dish");

        this.globalState = (GlobalState) getApplication();
        this.diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");

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

        if(dishName == null || dishPrice == null || dishImage == null) {
            Toast.makeText(getApplicationContext(), "You forgot one or more parameters!", Toast.LENGTH_SHORT).show();
            return;
        }

        Dish newDish = new Dish(dishName, dishPrice, dishRating, this.dishImage, this.globalState.getUsername());
        this.globalState.getDiningOption(this.diningOptionName).addDish(newDish);

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
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

}

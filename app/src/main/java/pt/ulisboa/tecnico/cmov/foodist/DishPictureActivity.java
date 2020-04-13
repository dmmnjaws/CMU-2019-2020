package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DishPictureActivity extends AppCompatActivity {

    private GlobalState globalState;
    private DishImage dishImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_picture);
        this.globalState = (GlobalState) getApplication();
    }

    @Override
    protected void onResume () {
        super.onResume();

        int imageId = (int) getIntent().getSerializableExtra("imageId");
        String dishName = (String) getIntent().getSerializableExtra("dishName");
        String diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");
        this.dishImage = this.globalState.getDishImage(diningOptionName, dishName, imageId);

        this.setTitle("FoodIST - " + this.dishImage.getDishName());

        ((ImageView) findViewById(R.id.dishImage)).setImageBitmap(this.dishImage.getBitmap());
        ((TextView) findViewById(R.id.author)).setText(this.dishImage.getUploaderUsername());

    }

}

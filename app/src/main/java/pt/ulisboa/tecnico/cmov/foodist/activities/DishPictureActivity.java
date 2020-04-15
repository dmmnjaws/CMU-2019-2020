package pt.ulisboa.tecnico.cmov.foodist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.library.DishImage;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class DishPictureActivity extends Activity {

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
        String campus = (String) getIntent().getSerializableExtra("campus");
        this.dishImage = this.globalState.getDishImage(campus, diningOptionName, dishName, imageId);

        ((ImageView) findViewById(R.id.dishImage)).setImageBitmap(this.dishImage.getBitmap());
        ((TextView) findViewById(R.id.author)).setText(this.dishImage.getUploaderUsername());

    }

}

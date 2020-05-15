package pt.ulisboa.tecnico.cmov.foodist.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.foodist.asynctasks.GetImageRemotely;
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

        ((TextView) findViewById(R.id.author)).setText(this.dishImage.getUploaderUsername());

        ImageView imageView = (ImageView) findViewById(R.id.dishImage);
        Bitmap imageFromCache = this.globalState.getCache().getImageFromCache(this.dishImage);

        if(imageFromCache == null){
            GetImageRemotely getImageRemotely = new GetImageRemotely(dishImage,this.globalState.getCache(),imageView,false);
            getImageRemotely.execute();
        } else{
            imageView.setImageBitmap(imageFromCache);
        }

        //((ImageView) findViewById(R.id.dishImage)).setImageBitmap(this.dishImage.getBitmap());

    }

}

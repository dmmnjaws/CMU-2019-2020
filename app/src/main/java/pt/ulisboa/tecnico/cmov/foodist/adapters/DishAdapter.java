package pt.ulisboa.tecnico.cmov.foodist.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.foodist.Cache;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.GetImageRemotely;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.foodist.R;
import pt.ulisboa.tecnico.cmov.library.DishImage;

public class DishAdapter extends ArrayAdapter<Dish> {

    Context context;
    int resource;
    ArrayList<Dish> dishes;
    Cache cache;

    public DishAdapter(Context context, int resource, ArrayList<Dish> dishes, Cache cache) {

        super(context, resource, dishes);
        this.context = context;
        this.resource = resource;
        this.dishes = dishes;
        this.cache = cache;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Dish dish = this.dishes.get(position);

        if ( convertView == null ) {
            convertView = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }

        TextView dishName = (TextView) convertView.findViewById(R.id.dishName);
        TextView dishCost = (TextView) convertView.findViewById(R.id.dishCost);
        RatingBar dishRatingBar = (RatingBar) convertView.findViewById(R.id.dishRatingBar);
        ImageView dishImage = (ImageView) convertView.findViewById(R.id.dishImage);

        dishName.setText(dish.getName());
        dishCost.setText(dish.getCost());
        dishRatingBar.setRating(dish.getRating());

        DishImage thumbnail = dish.getDishImage(0);
        if(thumbnail != null) {
            Bitmap imageFromCache = this.cache.getImageFromCache(thumbnail);
            if (imageFromCache == null) {
                System.out.println("Icon: " + thumbnail.getImageId());
                GetImageRemotely getImageRemotely = new GetImageRemotely(thumbnail, this.cache, dishImage, true);
                getImageRemotely.execute();
            } else {
                dishImage.setImageBitmap(Bitmap.createScaledBitmap(imageFromCache, 50, 50, false));
            }
        }

        return convertView;
    }
}

package pt.ulisboa.tecnico.cmov.foodist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class DishAdapter extends ArrayAdapter<Dish> {

    Context context;
    int resource;
    ArrayList<Dish> dishes;

    public DishAdapter(Context context, int resource, ArrayList<Dish> dishes) {

        super(context, resource, dishes);
        this.context = context;
        this.resource = resource;
        this.dishes = dishes;

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
        dishImage.setImageBitmap(dish.getThumbnail());

        return convertView;
    }
}

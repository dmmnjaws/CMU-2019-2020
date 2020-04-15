package pt.ulisboa.tecnico.cmov.foodist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.library.DishImage;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class DishImageAdapter extends ArrayAdapter<DishImage> {

    Context context;
    int resource;
    ArrayList<DishImage> imageIndexes;

    public DishImageAdapter(Context context, int resource, ArrayList<DishImage> imageIndexes) {

        super(context, resource, imageIndexes);
        this.context = context;
        this.resource = resource;
        this.imageIndexes = imageIndexes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DishImage dishImage = this.imageIndexes.get(position);

        if ( convertView == null ) {
            convertView = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }

        ImageView dishImageView = (ImageView) convertView.findViewById(R.id.dishImage);

        dishImageView.setImageBitmap(dishImage.getBitmap());


        return convertView;
    }
}


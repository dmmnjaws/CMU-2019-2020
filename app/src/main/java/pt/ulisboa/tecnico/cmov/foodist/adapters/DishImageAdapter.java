package pt.ulisboa.tecnico.cmov.foodist.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.foodist.Cache;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.GetImageRemotely;
import pt.ulisboa.tecnico.cmov.library.DishImage;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class DishImageAdapter extends ArrayAdapter<DishImage> {

    Context context;
    int resource;
    ArrayList<DishImage> imageIndexes;
    Cache cache;

    public DishImageAdapter(Context context, int resource, ArrayList<DishImage> imageIndexes, Cache cache) {

        super(context, resource, imageIndexes);
        this.context = context;
        this.resource = resource;
        this.imageIndexes = imageIndexes;
        this.cache = cache;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DishImage dishImage = this.imageIndexes.get(position);

        if ( convertView == null ) {
            convertView = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }

        ImageView dishImageView = (ImageView) convertView.findViewById(R.id.dishImage);

        Bitmap imageFromCache = this.cache.getImageFromCache(dishImage);

        if(imageFromCache == null){
            GetImageRemotely getImageRemotely = new GetImageRemotely(dishImage,this.cache,dishImageView,false);
            getImageRemotely.execute();
        } else{
            dishImageView.setImageBitmap(imageFromCache);
        }

        //dishImageView.setImageBitmap(dishImage.getBitmap());


        return convertView;
    }
}


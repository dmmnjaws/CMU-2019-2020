package pt.ulisboa.tecnico.cmov.foodist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.foodist.DiningPlace;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class DiningOptionAdapter extends ArrayAdapter<DiningPlace> {

    Context context;
    int resource;
    ArrayList<DiningPlace> diningPlaces;

    public DiningOptionAdapter(Context context, int resource, ArrayList<DiningPlace> diningPlaces) {

        super(context, resource, diningPlaces);
        this.context = context;
        this.resource = resource;
        this.diningPlaces = diningPlaces;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DiningPlace diningPlace = this.diningPlaces.get(position);

        if ( convertView == null ) {
            convertView = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }

        TextView diningOptionName = (TextView) convertView.findViewById(R.id.diningOptionName);
        TextView diningOptionAddress = (TextView) convertView.findViewById(R.id.diningOptionAddress);
        ImageView diningOptionImage = (ImageView) convertView.findViewById(R.id.diningOptionImage);

        diningOptionName.setText(diningPlace.getName());
        diningOptionAddress.setText(diningPlace.getAddress());
        diningOptionImage.setImageResource(diningPlace.getImageId());

        return convertView;
    }
}

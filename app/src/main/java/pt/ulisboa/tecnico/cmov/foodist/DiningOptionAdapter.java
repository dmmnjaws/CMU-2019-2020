package pt.ulisboa.tecnico.cmov.foodist;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DiningOptionAdapter extends ArrayAdapter<DiningOption> {

    Context context;
    int resource;
    ArrayList<DiningOption> diningOptions;

    public DiningOptionAdapter(Context context, int resource, ArrayList<DiningOption> diningOptions) {

        super(context, resource, diningOptions);
        this.context = context;
        this.resource = resource;
        this.diningOptions = diningOptions;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DiningOption diningOption = this.diningOptions.get(position);

        if ( convertView == null ) {
            convertView = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        }

        TextView diningOptionName = (TextView) convertView.findViewById(R.id.diningOptionName);
        TextView diningOptionAddress = (TextView) convertView.findViewById(R.id.diningOptionAddress);
        ImageView diningOptionImage = (ImageView) convertView.findViewById(R.id.diningOptionImage);

        diningOptionName.setText(diningOption.getName());
        diningOptionAddress.setText(diningOption.getAddress());
        diningOptionImage.setImageResource(diningOption.getImageId());

        return convertView;
    }
}

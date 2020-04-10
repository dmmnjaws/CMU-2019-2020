package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DiningOptionsActivity extends AppCompatActivity {

    private GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_options);
        this.setTitle("FoodIST - Dining Options");

        this.globalState = (GlobalState) getApplication();

        ListView listOfDiningPlaces = (ListView) findViewById(R.id.listOfDiningPlaces);

        ArrayList<DiningOption> diningOptions = this.globalState.getDiningOptions();

        this.globalState.populateForTest();

        DiningOptionAdapter diningOptionAdapter = new DiningOptionAdapter(getApplicationContext(), R.layout.list_row_dining_option, diningOptions);
        listOfDiningPlaces.setAdapter(diningOptionAdapter);

        listOfDiningPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DiningOption item = (DiningOption) parent.getItemAtPosition(position);
                Intent intent = new Intent(DiningOptionsActivity.this, DiningPlaceActivity.class);
                intent.putExtra("diningOptionName", item.getName());
                startActivity(intent);
            }

        });

    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DiningOptionsActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

}

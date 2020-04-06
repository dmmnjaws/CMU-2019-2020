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

        //FOR TEST PURPOSES:
        DiningOption copacabana = new DiningOption("Copacabana", "Rua da Joaquina", R.drawable.ic_options_threedots_background);
        copacabana.addDish(new Dish("Ensopado de Tetas", "20 paus", 1, R.drawable.ic_options_threedots_background,"Bras"));
        copacabana.addDish(new Dish("Gaijas", "523 paus", 1, R.drawable.ic_options_threedots_background,this.globalState.getUsername()));
        diningOptions.add(copacabana);
        diningOptions.add(new DiningOption("Jucaca", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Garfunkle", "Avenida Gay", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Kutxarra", "Rua da Joaquina", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Katuqui", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Merekete", "Avenida Gay", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Kunami", "Rua da Joaquina", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Konami", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Santos G", "Avenida Gay", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Restaurante do José Brás", "Rua da Joaquina", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Punanirolls", "Rua da Maria Coxa", R.drawable.ic_options_threedots_background));
        diningOptions.add(new DiningOption("Sexappeal Bar", "Avenida Gay", R.drawable.ic_options_threedots_background));

        DiningOptionAdapter diningOptionAdapter = new DiningOptionAdapter(getApplicationContext(), R.layout.list_row_dining_option, diningOptions);
        listOfDiningPlaces.setAdapter(diningOptionAdapter);

        listOfDiningPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DiningOption item = (DiningOption) parent.getItemAtPosition(position);
                Intent intent = new Intent(DiningOptionsActivity.this, DiningPlaceActivity.class);
                intent.putExtra("diningOption", item);
                startActivity(intent);
            }

        });

    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DiningOptionsActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

}

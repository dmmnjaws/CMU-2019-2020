package pt.ulisboa.tecnico.cmov.foodist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.ulisboa.tecnico.cmov.foodist.SimWifiP2pBroadcastReceiver;
import pt.ulisboa.tecnico.cmov.library.DiningPlace;
import pt.ulisboa.tecnico.cmov.library.Dish;
import pt.ulisboa.tecnico.cmov.foodist.adapters.DishAdapter;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class DiningPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SimWifiP2pManager.PeerListListener {

    private DiningPlace diningPlace;
    private GlobalState globalState;
    private String campus;
    private String queueTime;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private boolean mBound = false;
    private SimWifiP2pBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_place);
        this.setTitle("FoodIST - Dining Place");
        this.globalState = (GlobalState) getApplication();
        this.campus = (String) getIntent().getSerializableExtra("campus");

        ListView listOfDishes = (ListView) findViewById(R.id.listOfDishes);

        listOfDishes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Dish item = (Dish) parent.getItemAtPosition(position);
                Intent intent = new Intent(DiningPlaceActivity.this, DishActivity.class);
                intent.putExtra("diningOptionName", item.getDiningPlace());
                intent.putExtra("dishName", item.getName());
                intent.putExtra("campus", diningPlace.getCampus());
                startActivity(intent);
            }

        });

        Spinner spinner = findViewById(R.id.chooseDiningPlaceSpinner);

        String[] diningOptionNames = this.globalState.getDiningOptionNames(this.campus);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diningOptionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final String diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");

        spinner.setSelection(this.globalState.getDiningOptionIndex(this.campus, diningOptionName));
        spinner.setOnItemSelectedListener(this);


        View buttonInflater = (View) getLayoutInflater().inflate(R.layout.upload_button,null);
        ImageButton addDishButton = (ImageButton) buttonInflater.findViewById(R.id.upload);
        listOfDishes.addFooterView(buttonInflater);

        addDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent intent = new Intent(DiningPlaceActivity.this, DishUploadActivity.class);
                intent.putExtra("diningOptionName", diningPlace.getName());
                intent.putExtra("campus", diningPlace.getCampus());
                startActivity(intent);
            }
        });

        // LIKE IN LAB 4: register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new SimWifiP2pBroadcastReceiver(this);
        registerReceiver(mReceiver, filter);

        // LIKE IN LAB 4: Bind WiFi Direct
        Intent intent = new Intent(getBaseContext(), SimWifiP2pService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume () {
        super.onResume();

        String diningOptionName = ((Spinner) findViewById(R.id.chooseDiningPlaceSpinner)).getSelectedItem().toString();

        populateActivity(diningOptionName);

    }

    @Override
    protected void onStop(){
        super.onStop();

        // LIKE IN LAB 4: Unbind WiKi Direct
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    public void optionsButtonOnClick(View view) {

        Intent intent = new Intent(DiningPlaceActivity.this, UserProfileActivity.class);
        startActivity(intent);

    }

    public void populateActivity (String diningOptionName){

        this.diningPlace = this.globalState.getDiningOption(this.campus, diningOptionName);

        ListView listOfDishes = (ListView) findViewById(R.id.listOfDishes);

        ((TextView) findViewById(R.id.diningOptionSchedule)).setText("Schedule: " + this.diningPlace.getSchedule(this.globalState.getActualCategoryIndex()));
        ((TextView) findViewById(R.id.diningOptionQueueTime)).setText("Average queue time: " + this.diningPlace.getQueueTime());

        ArrayList<Dish> dishes = this.diningPlace.getDishes();
        DishAdapter dishAdapter = new DishAdapter(getApplicationContext(), R.layout.list_row_dish, dishes);
        listOfDishes.setAdapter(dishAdapter);
    }

    public void checkLocationOnClick(View view){
        Intent intent = new Intent(DiningPlaceActivity.this, DiningPlaceMapActivity.class);
        intent.putExtra("coordinates", this.diningPlace.getCoordinates());
        intent.putExtra( "diningOptionName", this.diningPlace.getName());
        intent.putExtra("campus", this.diningPlace.getCampus());
        startActivity(intent);

        //ALTERNATIVE (LIGHTER):
        //Skip the entirety of DiningPlaceMapActivity and go straight to directions:

        /*
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", this.diningPlace.getCoordinates()[0], this.diningPlace.getCoordinates()[1], this.diningPlace.getName());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
         */
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        populateActivity(parent.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // LIKE IN LAB 4: callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mManager = new SimWifiP2pManager(new Messenger(service));
            mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
            mBound = true;

            // LIKE IN LAB 4: Request peers in range
            if (mBound) {
                mManager.requestPeers(mChannel, DiningPlaceActivity.this);
            } else {
                Toast.makeText(getBaseContext(), "Could not calculate queue time for this restaurant.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        // LIKE IN LAB 4: when peers detected

        StringBuilder peersStr = new StringBuilder();

        int queueTime = 0;

        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            queueTime++;
        }

        this.diningPlace.setQueueTime(queueTime + " minutes");
        ((TextView) findViewById(R.id.diningOptionQueueTime)).setText("Average queue time: " + this.diningPlace.getQueueTime());

    }
}

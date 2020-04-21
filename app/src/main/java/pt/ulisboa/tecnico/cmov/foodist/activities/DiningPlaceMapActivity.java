package pt.ulisboa.tecnico.cmov.foodist.activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;
import pt.ulisboa.tecnico.cmov.library.DiningPlace;

public class DiningPlaceMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double[] coordinates;
    private String diningOptionName;
    private String campus;
    private GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_place_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        this.globalState = (GlobalState) getApplication();
        this.coordinates = (double[]) getIntent().getSerializableExtra("coordinates");
        this.diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");
        this.campus = (String) getIntent().getSerializableExtra("campus");

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mMap = googleMap;
        LatLng diningPlaceCoordinates = new LatLng(coordinates[0], coordinates[1]);
        Marker thisMarker = this.mMap.addMarker(new MarkerOptions().position(diningPlaceCoordinates).title(diningOptionName));

        this.mMap.moveCamera(CameraUpdateFactory.newLatLng(diningPlaceCoordinates));
        thisMarker.showInfoWindow();
        this.mMap.setMinZoomPreference(16);


        for(DiningPlace diningPlace: this.globalState.getDiningOptions(this.campus)){
            this.mMap.addMarker(new MarkerOptions().position(new LatLng(diningPlace.getCoordinates()[0], diningPlace.getCoordinates()[1])).title(diningPlace.getName()));
        }
    }
}

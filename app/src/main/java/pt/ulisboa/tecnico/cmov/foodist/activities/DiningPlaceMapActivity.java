package pt.ulisboa.tecnico.cmov.foodist.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pt.ulisboa.tecnico.cmov.foodist.R;

public class DiningPlaceMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double[] coordinates;
    private String diningOptionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_place_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        this.coordinates = (double[]) getIntent().getSerializableExtra("coordinates");
        this.diningOptionName = (String) getIntent().getSerializableExtra("diningOptionName");
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng diningPlace = new LatLng(coordinates[0], coordinates[1]);
        mMap.addMarker(new MarkerOptions().position(diningPlace).title(diningOptionName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(diningPlace));
        mMap.setMinZoomPreference(15);
    }
}

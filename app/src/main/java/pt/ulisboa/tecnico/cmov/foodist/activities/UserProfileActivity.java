package pt.ulisboa.tecnico.cmov.foodist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;
import pt.ulisboa.tecnico.cmov.foodist.asynctasks.AddPreferencesRemotely;

public class UserProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int GALLERY_REQUEST_CODE = 100;
    private GlobalState globalState;
    private Map<String, Boolean> preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        this.setTitle("FoodIST - User Profile");
        this.globalState = (GlobalState) getApplication();

        Spinner spinner = findViewById(R.id.category);

        String[] categoryList = this.globalState.getCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(this.globalState.getActualCategoryIndex());
        spinner.setOnItemSelectedListener(this);

        TextView usernameView = (TextView) findViewById(R.id.username);
        usernameView.setText(this.globalState.getUsername());

        if (this.globalState.getProfilePicture() != null){
            ((ImageView) findViewById(R.id.profilePicture)).setImageBitmap(this.globalState.getProfilePicture());
        }

        this.preferences = this.globalState.getPreferences();
        ((CheckBox) findViewById(R.id.vegetarianCheckBox)).setChecked(this.preferences.get("Vegetarian"));
        ((CheckBox) findViewById(R.id.veganCheckBox)).setChecked(this.preferences.get("Vegan"));
        ((CheckBox) findViewById(R.id.meatCheckBox)).setChecked(this.preferences.get("Meat"));
        ((CheckBox) findViewById(R.id.fishCheckBox)).setChecked(this.preferences.get("Fish"));
    }

    @Override
    protected void onPause(){
        super.onPause();

        Map<String, Boolean> actualPreferences = new HashMap<>();
        actualPreferences.put("Vegetarian", ((CheckBox) findViewById(R.id.vegetarianCheckBox)).isChecked());
        actualPreferences.put("Vegan", ((CheckBox) findViewById(R.id.veganCheckBox)).isChecked());
        actualPreferences.put("Meat", ((CheckBox) findViewById(R.id.meatCheckBox)).isChecked());
        actualPreferences.put("Fish", ((CheckBox) findViewById(R.id.fishCheckBox)).isChecked());

        if(!actualPreferences.equals(preferences)){
            this.globalState.setPreferences(actualPreferences);
            AddPreferencesRemotely addPreferencesRemotely = new AddPreferencesRemotely(this.globalState.getUsername(), actualPreferences);
            addPreferencesRemotely.execute();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        this.globalState.setActualCategory(parent.getSelectedItem().toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void addImageOnClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                Bitmap profilePicture = BitmapFactory.decodeStream(imageStream);
                this.globalState.setProfilePicture(profilePicture);
                ((ImageView) findViewById(R.id.profilePicture)).setImageBitmap(profilePicture);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}

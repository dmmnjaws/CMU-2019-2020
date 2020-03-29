package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class UserProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private GlobalState globalState;

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
    }

    public void changeUsername(View view) {
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        this.globalState.setActualCategory(parent.getSelectedItem().toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}

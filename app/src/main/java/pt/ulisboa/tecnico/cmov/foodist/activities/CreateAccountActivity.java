package pt.ulisboa.tecnico.cmov.foodist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class CreateAccountActivity extends AppCompatActivity {

    GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        this.globalState = (GlobalState) getApplication();
        this.setTitle("FoodIST - Creating Account...");
    }

    public void createAccountButtonClick(View view) {
        String newUsername = ((EditText) findViewById(R.id.newUsername)).getText().toString();
        String newPassword = ((EditText) findViewById(R.id.newPassword)).getText().toString();

        if (this.globalState.createAccount(newUsername, newPassword)){
            Intent intent = new Intent(CreateAccountActivity.this, DiningOptionsActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
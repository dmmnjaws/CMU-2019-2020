package pt.ulisboa.tecnico.cmov.foodist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;
import pt.ulisboa.tecnico.cmov.foodist.SimWifiP2pBroadcastReceiver;

public class LogInActivity extends AppCompatActivity {

    private GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        this.setTitle("FoodIST - Log In");
        this.globalState = (GlobalState) getApplication();

        if (this.globalState.isLoggedIn() == true) {

            Intent intent = new Intent(LogInActivity.this, DiningOptionsActivity.class);
            startActivity(intent);

        }

    }

    public void logInButtonOnClick(View view) {
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        this.globalState.login(username, password);

        if(this.globalState.isLoggedIn()){
            Intent intent = new Intent(LogInActivity.this, DiningOptionsActivity.class);
            startActivity(intent);
        }
    }
}

package pt.ulisboa.tecnico.cmov.foodist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    private GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        this.setTitle("FoodIST - Log In");
        this.globalState = (GlobalState) getApplication();

        if (this.globalState.isLoggedIn() == true) {

            this.onStop();
            Intent intent = new Intent(LogInActivity.this, DiningOptionsActivity.class);
            startActivity(intent);

        }

    }

    public void logInButtonOnClick(View view) {
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        this.globalState.login(username, password);
        //Log.i("username and password", username + " | " + password);

        Intent intent = new Intent(LogInActivity.this, DiningOptionsActivity.class);
        startActivity(intent);
    }
}

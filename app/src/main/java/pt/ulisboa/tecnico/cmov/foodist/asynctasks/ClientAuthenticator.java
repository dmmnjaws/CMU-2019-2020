package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.library.DishesView;

public class ClientAuthenticator extends AsyncTask {

    private String usernameAuthenticate;
    private String passwordAuthenticate;

    public ClientAuthenticator(String usernameAuthenticate, String passwordAuthenticate){
        this.usernameAuthenticate = usernameAuthenticate;
        this.passwordAuthenticate = passwordAuthenticate;
    }

    protected Object doInBackground(Object[] objects) {

        boolean isAuthenticated = false;

        try {

            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream.writeObject("authenticate");
            outputStream.writeObject(this.usernameAuthenticate);
            outputStream.writeObject(this.passwordAuthenticate);
            isAuthenticated = (boolean) inputStream.readObject();
            clientSocket.close();
            Log.d("DEBUG:", "DEBUG - DID ASYNC SERVER READ");
        } catch (Exception e) {
            e.printStackTrace();

        }

        return isAuthenticated;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("DEBUG:", "DEBUG - DID ASYNC CLIENT WRITE");

    }
}


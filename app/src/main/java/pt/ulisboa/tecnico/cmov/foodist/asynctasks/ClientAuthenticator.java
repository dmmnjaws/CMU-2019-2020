package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.library.DishesView;

public class ClientAuthenticator extends AsyncTask {

    private GlobalState globalState;

    public ClientAuthenticator(GlobalState globalState){
        this.globalState = globalState;
    }

    protected Object doInBackground(Object[] objects) {

        boolean isAuthenticated = false;

        try {

            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream.writeObject("authenticate");
            outputStream.writeObject(this.globalState.getUsername());
            outputStream.writeObject(this.globalState.getPassword());
            isAuthenticated = (boolean) inputStream.readObject();
            if (isAuthenticated){
                this.globalState.setPreferences((Map<String, Boolean>) inputStream.readObject());
            }

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


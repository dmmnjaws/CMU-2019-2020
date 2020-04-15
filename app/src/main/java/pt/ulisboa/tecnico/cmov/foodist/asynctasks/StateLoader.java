package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.library.DiningPlace;

public class StateLoader extends AsyncTask {

    private Map<String, ArrayList<DiningPlace>> state;

    protected Object doInBackground(Object[] objects) {
        try {

            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream.writeObject("loadState");
            this.state = (Map<String, ArrayList<DiningPlace>>) inputStream.readObject();
            clientSocket.close();
            Log.d("DEBUG:", "NIGGA");
        } catch (Exception e) {
            e.printStackTrace();

        }


        return null;
    }

    public Map<String, ArrayList<DiningPlace>> getState(){ return this.state; }
}


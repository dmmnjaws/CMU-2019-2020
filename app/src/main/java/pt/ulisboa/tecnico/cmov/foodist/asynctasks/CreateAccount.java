package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CreateAccount extends AsyncTask {

    private String newUsername;
    private String newPassword;

    public CreateAccount(String newUsername, String newPassword){
        this.newUsername = newUsername;
        this.newPassword = newPassword;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        boolean isCreated = false;

        try {

            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream.writeObject("createAccount");
            outputStream.writeObject(this.newUsername);
            outputStream.writeObject(this.newPassword);
            isCreated = (boolean) inputStream.readObject();
            clientSocket.close();
            Log.d("DEBUG:", "DEBUG - DID ASYNC SERVER READ");
        } catch (Exception e) {
            e.printStackTrace();

        }

        return isCreated;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("DEBUG:", "DEBUG - DID ASYNC CLIENT WRITE");

    }
}

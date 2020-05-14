package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.foodist.R;

public class BluetoothBeaconDetector extends AsyncTask implements SimWifiP2pManager.PeerListListener {

    private GlobalState globalState;
    String command;
    String beaconName;

    public BluetoothBeaconDetector(GlobalState globalState){
        this.globalState = globalState;

    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {
            Socket clientSocket = new Socket("10.0.2.2", 8000);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

            // LIKE IN LAB 4: Request peers in range
            if (this.globalState.getMbound()) {
                this.globalState.getMManager().requestPeers(this.globalState.getMChannel(), this);
                Thread.sleep(3000);
            } else {
                Toast.makeText(this.globalState.getBaseContext(), "Could not calculate queue times.", Toast.LENGTH_SHORT).show();
            }

            if(!(this.command == null || this.beaconName == null)){
                outputStream.writeObject(this.command);
                outputStream.writeObject(this.globalState.getUsername());
                outputStream.writeObject(this.beaconName);
                outputStream.writeObject(Long.valueOf(System.currentTimeMillis()));
            }

            clientSocket.close();
            Log.d("DEBUG:", "DEBUG - DID ASYNC SERVER READ");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList) {
        // LIKE IN LAB 4: when peers detected...
        Collection<SimWifiP2pDevice> beaconsList = simWifiP2pDeviceList.getDeviceList();

        try {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.globalState.getBaseContext());

            if (beaconsList.size() == 0){
                this.command = "leavingQueue";
                this.beaconName = this.globalState.getLastKnownBeacon();
                this.globalState.setLastKnownBeacon(null);

                if(this.globalState.isLoggedIn()){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this.globalState.getBaseContext(), "FoodIST")
                            .setSmallIcon(R.drawable.smallicon)
                            .setContentTitle("FoodIST")
                            .setStyle(new NotificationCompat.BigTextStyle().bigText("Hey! You're not waiting in the queue anymore, are you? Take a picture of the food you're about to eat and rate it! Also, again, share it with your friends!"))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    notificationManager.notify(100, builder.build());

                }

            } else {
                this.command = "joiningQueue";
                this.beaconName = (beaconsList.iterator().next()).deviceName;
                this.globalState.setLastKnownBeacon(this.beaconName);

                if(this.globalState.isLoggedIn()){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this.globalState.getBaseContext(), "FoodIST")
                            .setSmallIcon(R.drawable.smallicon)
                            .setContentTitle("FoodIST")
                            .setStyle(new NotificationCompat.BigTextStyle().bigText("Hey! Since you're in a queue, consider submitting the dish you're ordering to FoodIST! Or, of course, if it's there on the menu already, consider rating it and sharing with your friends!"))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    notificationManager.notify(101, builder.build());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

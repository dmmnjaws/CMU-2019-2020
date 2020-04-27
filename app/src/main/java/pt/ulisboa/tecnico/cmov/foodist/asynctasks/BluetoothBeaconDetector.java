package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.foodist.GlobalState;

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

            if (beaconsList.size() == 0){
                this.command = "leavingQueue";
                this.beaconName = this.globalState.getLastKnownBeacon();
                this.globalState.setLastKnownBeacon(null);

            } else {
                this.command = "joiningQueue";
                this.beaconName = (beaconsList.iterator().next()).deviceName;
                this.globalState.setLastKnownBeacon(this.beaconName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

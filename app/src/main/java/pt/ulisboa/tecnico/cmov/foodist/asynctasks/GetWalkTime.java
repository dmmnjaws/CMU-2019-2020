package pt.ulisboa.tecnico.cmov.foodist.asynctasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import pt.ulisboa.tecnico.cmov.foodist.GlobalState;
import pt.ulisboa.tecnico.cmov.library.DiningPlace;

public class GetWalkTime extends AsyncTask {

    private GlobalState globalState;

    public GetWalkTime(GlobalState globalState){
        this.globalState = globalState;
    }

    protected Object doInBackground(Object[] objects) {

        walkTimeRequest("Alameda", this.globalState.getUserCoordinates());
        walkTimeRequest("Taguspark", this.globalState.getUserCoordinates());

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("DEBUG:", "DEBUG - UPDATED WALKTIME");

    }

    public void walkTimeRequest(String campus, String userCoordinates) {
        String destinations = "";

        for (DiningPlace diningPlace : this.globalState.getDiningOptions(campus)) {
            if(destinations.equals("")){
                destinations += diningPlace.getCoordinates()[0] + "," + diningPlace.getCoordinates()[1];
            }else{
                destinations += "|" + diningPlace.getCoordinates()[0] + "," + diningPlace.getCoordinates()[1];
            }
        }

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("maps.googleapis.com")
                    .appendPath("maps")
                    .appendPath("api")
                    .appendPath("distancematrix")
                    .appendPath("json")
                    .appendQueryParameter("origins", userCoordinates)
                    .appendQueryParameter("destinations", destinations)
                    .appendQueryParameter("mode", "walking")
                    .appendQueryParameter("key", "AIzaSyDE3PRemYzj_Gvac01M4MwLvR4XHXSdzyM");

            String myUrl = builder.build().toString();

            JSONObject jsonObject = getJSONObjectFromURL(myUrl);

            JSONArray results  = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");

            for(int i = 0; i< results.length(); i++){
                String walkTime = results.getJSONObject(i).getJSONObject("duration").getString("text");
                this.globalState.getDiningOptions(campus).get(i).setWalkingTime(walkTime);

            }

            System.out.println(jsonObject);

        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();

        return new JSONObject(jsonString);
    }
}

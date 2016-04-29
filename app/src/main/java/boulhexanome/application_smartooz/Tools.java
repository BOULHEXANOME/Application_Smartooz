package boulhexanome.application_smartooz;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import boulhexanome.application_smartooz.Model.Circuit;

/**
 * Created by Aiebobo on 28/04/2016.
 */
public class Tools {

    public static JsonObject parseJson(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        Gson gson = new Gson();
        try {
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            String line = stringBuilder.toString();
            JsonParser parser = new JsonParser();
            JsonElement jsnelement = (JsonElement) parser.parse(line);
            JsonObject jsnobject = jsnelement.getAsJsonObject();
            in.close();
            return jsnobject;
        } catch (IOException e) {
            Log.e("IOException", "identifyUser : IOException");
            e.printStackTrace();
            return null;
        }
    }

    public static URL generateGoogleMapURL(Circuit circuit){

        String url_string = "https://maps.googleapis.com/maps/api/directions/json?";

        String origin = "origin="
                + circuit.getPlaces().get(0).getPosition().latitude
                + ","
                + circuit.getPlaces().get(0).getPosition().longitude;

        String destination = "&destination="
                + circuit.getPlaces().get(circuit.getPlaces().size() -1).getPosition().latitude
                + ","
                + circuit.getPlaces().get(circuit.getPlaces().size() -1).getPosition().longitude;

        String waypoints = "&waypoints=";
        for (int i = 1; i <circuit.getPlaces().size()-1; i++){
            waypoints = waypoints
                    + circuit.getPlaces().get(i).getPosition().latitude
                    + ","
                    + circuit.getPlaces().get(i).getPosition().longitude
                    + "|";
        }
        waypoints = waypoints.substring(0,waypoints.length()-1);

        String mode = "&mode=walking";

        try {
            URL url = new URL(url_string+origin+destination+waypoints+mode);
            return url;
        } catch (MalformedURLException e) {
            Log.e("URLGenerator","URL mal formé");
            e.printStackTrace();
            return null;
        }
    }
}

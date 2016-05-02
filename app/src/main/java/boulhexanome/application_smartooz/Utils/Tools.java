package boulhexanome.application_smartooz.Utils;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.android.PolyUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.Activities.VisiterLyonActivity;
import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.WebServices.PostTask;

/**
 * Created by Aiebobo on 28/04/2016.
 */
public class Tools {

    public static JsonObject parseJson(InputStream is) throws UnsupportedEncodingException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;

        try {
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            String line = stringBuilder.toString();
            JsonParser parser = new JsonParser();
            JsonElement jsnelement = (JsonElement) parser.parse(line);
            JsonObject jsnobject = jsnelement.getAsJsonObject();
            in.close();
            is.close();
            return jsnobject;
        } catch (IOException e) {
            Log.e("IOException", "identifyUser : IOException");
            e.printStackTrace();
            return null;
        }
    }

    public static URL generateGoogleMapURL(ArrayList<Marker> markers){

        String url_string = "https://maps.googleapis.com/maps/api/directions/json?";

        String origin = "origin="
                + markers.get(0).getPosition().latitude
                + ","
                + markers.get(0).getPosition().longitude;

        String destination = "&destination="
                + markers.get(markers.size() -1).getPosition().latitude
                + ","
                + markers.get(markers.size() -1).getPosition().longitude;

        String waypoints = "";
        if (markers.size()>2) {
            waypoints = "&waypoints=optimize:true|";
            for (int i = 1; i < markers.size() - 1; i++) {
                waypoints = waypoints
                        + markers.get(i).getPosition().latitude
                        + ","
                        + markers.get(i).getPosition().longitude
                        + "|";
            }
            waypoints = waypoints.substring(0, waypoints.length() - 1);
        }

        String mode = "&mode=walking";

        try {
            URL url = new URL(url_string+origin+destination+waypoints+mode);
            System.out.println(url);
            return url;
        } catch (MalformedURLException e) {
            Log.e("URLGenerator","URL mal formé");
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

        String waypoints = "";
        if (circuit.getPlaces().size()>2) {
            waypoints = "&waypoints=optimize:true|";
            for (int i = 1; i < circuit.getPlaces().size() - 1; i++) {
                waypoints = waypoints
                        + circuit.getPlaces().get(i).getPosition().latitude
                        + ","
                        + circuit.getPlaces().get(i).getPosition().longitude
                        + "|";
            }
            waypoints = waypoints.substring(0, waypoints.length() - 1);
        }

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

    public static List<LatLng> decodeDirections(JsonObject jsonObject) {
        JsonArray resultsArray = jsonObject.getAsJsonArray("routes");
        JsonObject routes = resultsArray.get(0).getAsJsonObject();
        String show = routes.get("overview_polyline").getAsJsonObject().get("points").getAsString();
        show = show.replace("\\\\", "\\");
        try {
            List<LatLng> listePoints = PolyUtil.decode(show);
            return listePoints;
        } catch(Exception e) {
            Log.e("googleMapError", "Error while decoding polyline");
            return null;
        }
    }

}

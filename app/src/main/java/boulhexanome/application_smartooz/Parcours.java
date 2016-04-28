package boulhexanome.application_smartooz;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.WebServices.GetItineraire;
import boulhexanome.application_smartooz.WebServices.Inscription;

public class Parcours extends AppCompatActivity implements OnMapReadyCallback, GetItineraire.AsyncResponse {

    private GoogleMap mMap;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcours);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Parcours");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        Place pointA = new Place(45.781000,4.876105,"Point A");
        Place pointB = new Place(45.759289,4.888261,"Point B");


        Marker markerA = mMap.addMarker(pointA.toMarkerOptions());
        Marker markerB = mMap.addMarker(pointB.toMarkerOptions());

        JsonObject params = new JsonObject();
        params.addProperty("lat1",markerA.getPosition().latitude);
        params.addProperty("long1",markerA.getPosition().longitude);
        params.addProperty("lat2",markerB.getPosition().latitude);
        params.addProperty("long2",markerB.getPosition().longitude);

        GetItineraire getItineraire = new GetItineraire();
        getItineraire.delegate = this;
        getItineraire.execute(params);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void processFinish(JsonObject results) {
        JsonArray resultsArray = results.getAsJsonArray("routes");
        JsonObject routes = resultsArray.get(0).getAsJsonObject();
        String show = routes.get("overview_polyline").getAsJsonObject().get("points").toString();
        show = show.substring(1,show.length()-1);
        show = show.replace("\\\\", "\\");

        System.out.println(show);

        List<LatLng> listePoints = PolyUtil.decode(show);
        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .addAll(listePoints));

        System.out.println(listePoints.toString());
    }
}

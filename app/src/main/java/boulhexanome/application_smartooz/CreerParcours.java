package boulhexanome.application_smartooz;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.database.MergeCursor;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.Place;
import boulhexanome.application_smartooz.WebServices.GetItineraire;

import static boulhexanome.application_smartooz.Tools.decodeDirections;
import static boulhexanome.application_smartooz.Tools.generateGoogleMapURL;

public class CreerParcours extends AppCompatActivity implements OnMapReadyCallback, GetItineraire.AsyncResponse {

    private GoogleMap mMap;
    private ActionBar toolbar;
    private ActionMode mActionMode;

    Polyline currentLine;

    ArrayList<Marker> markers;

    boolean modeAjout;

    Circuit parcours;
    ArrayList<Place> places = new ArrayList<Place>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_parcours);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Créer Parcours");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        currentLine = null;
        modeAjout = false;
        markers = new ArrayList<Marker>();;

        parcours = new Circuit();

        final FloatingActionButton ajouterPI = (FloatingActionButton) findViewById(R.id.action_ajouterPI);
        final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_ajouter_etape, menu);
                modeAjout = true;
                return true;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle("Ajouter une étape");
                ajouterPI.setImageDrawable(getDrawable(R.drawable.ic_done_white_24dp));
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ajouterPI.setImageDrawable(getDrawable(R.drawable.ic_add_location_white_24dp));
                modeAjout = false;
            }
        };
        ajouterPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the CAB using the ActionMode.Callback defined above
                if (modeAjout == false) {
                    mActionMode = CreerParcours.this.startSupportActionMode(mActionModeCallback);
                    v.setSelected(true);
                } else {
                    mActionMode.finish();
                }
            }
        });

        final FloatingActionButton visualiser = (FloatingActionButton) findViewById(R.id.action_visualiser);
        visualiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markers.size()>=2){
                    if (currentLine != null){
                        currentLine.remove();
                    }
                    visualize();
                }
            }
        });

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

        mMap.moveCamera(CameraUpdateFactory
                .newLatLngBounds((new LatLngBounds(new LatLng(45.720301, 4.779128), new LatLng(45.797678, 4.926584))), 0));

        Place pointA = new Place(new LatLng(45.770861, 4.873173),"Point A");
        Place pointB = new Place(new LatLng(45.763579, 4.890372),"Point B");
        Place pointC = new Place(new LatLng(45.758049, 4.882280),"Point C");
        Place pointD = new Place(new LatLng(45.769907, 4.863175),"Point D");

        places.add(pointA);
        places.add(pointB);
        places.add(pointC);
        places.add(pointD);

        pointA.setDescription("Point d'intérêt méga stylé !");

        mMap.addMarker(pointA.toMarkerOptions());
        mMap.addMarker(pointB.toMarkerOptions());
        mMap.addMarker(pointC.toMarkerOptions());
        mMap.addMarker(pointD.toMarkerOptions());

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (modeAjout == false){
                    return false;
                } else {
                    if (markers.contains(marker)){
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        markers.remove(marker);
                        return true;
                    } else {
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        markers.add(marker);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                        return true;
                    }
                }
            }
        });



        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(final Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
                v.setFocusableInTouchMode(true);

                LatLng position = arg0.getPosition();
                Place placeMarked = null;
                for (int i = 0; i < places.size(); i++){
                    if (places.get(i).getPosition().equals(position)){
                        placeMarked = places.get(i);
                        i = places.size();
                    }
                }

                TextView title = (TextView) v.findViewById(R.id.title_place);
                TextView description = (TextView) v.findViewById(R.id.description);
                TextView noteOn5 = (TextView) v.findViewById(R.id.noteon5);

                title.setText(placeMarked.getName());
                description.setText(placeMarked.getDescription());
                noteOn5.setText(String.valueOf(placeMarked.getNoteOn5()));

                final Place finalPlaceMarked = placeMarked;

                // Returning the view containing InfoWindow contents
                return v;

            }
        });

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_creer_parcours, menu);
        return true;
    }

    @Override
    public void processFinish(JsonObject results) {
        List<LatLng> listePoints = decodeDirections(results);
        currentLine = mMap.addPolyline(new PolylineOptions()
                .addAll(listePoints));
    }

    public void visualize(){
        GetItineraire getItineraire = new GetItineraire();
        getItineraire.delegate = this;
        URL url = generateGoogleMapURL(markers);
        getItineraire.execute(url);
    }
}

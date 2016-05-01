package boulhexanome.application_smartooz.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.Place;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.Utils.Tools;
import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.WebServices.GetTask;
import boulhexanome.application_smartooz.WebServices.PostTask;

public class CreerParcours extends AppCompatActivity implements OnMapReadyCallback, PostTask.AsyncResponse, GetTask.AsyncResponse {

    private static final int ASK_FOR_ACCESS_COARSE_LOCATION = 1;
    private static final int ASK_FOR_ACCESS_FINE_LOCATION = 2;
    private GoogleMap mMap;
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
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Créer Parcours");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        currentLine = null;
        modeAjout = false;
        markers = new ArrayList<Marker>();
        ;

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

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                mode.setTitle("Ajouter une étape");
                ajouterPI.setImageResource(R.drawable.ic_done_white_24dp);
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ajouterPI.setImageResource(R.drawable.ic_add_location_white_24dp);
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
                if (markers.size() >= 2) {
                    if (currentLine != null) {
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

        LatLngBounds GRAND_LYON = new LatLngBounds(
                new LatLng(45.720301, 4.779128), new LatLng(45.797678, 4.926584));

        mMap.moveCamera(CameraUpdateFactory
                .newLatLngBounds(GRAND_LYON,10));

        getPlaces();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ASK_FOR_ACCESS_COARSE_LOCATION);
        }
        if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ASK_FOR_ACCESS_FINE_LOCATION);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        try{
            mMap.setMyLocationEnabled(true);

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (!modeAjout){
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                        return true;
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
                    TextView tags = (TextView) v.findViewById(R.id.tags_infowindow);

                    title.setText(placeMarked.getName());
                    description.setText(placeMarked.getDescription());
                    noteOn5.setText("Note : " + String.valueOf(placeMarked.getNoteOn5()) + " / 5");


                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Tags : ");
                    for (int i = 0; i < placeMarked.getKeywords().size(); i++) {
                        stringBuilder.append(placeMarked.getKeywords().get(i) + " ");
                    }
                    stringBuilder.deleteCharAt(stringBuilder.length()-1);
                    tags.setText(stringBuilder.toString());

                    final Place finalPlaceMarked = placeMarked;

                    // Returning the view containing InfoWindow contents
                    return v;
                }
            });
        }catch (SecurityException e){
            System.out.println(e);
        }

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

        if (id == R.id.action_rechercher) {

        }

        if (id == R.id.action_save) {

            for (int i = 0; i < markers.size(); i++){
                for (int j = 0; j < places.size(); j++){
                    if (places.get(j).getPosition().equals(markers.get(i).getPosition())){
                        User.getInstance().getCircuit_en_creation().addPlace(places.get(i));
                        j = places.size();
                    }
                }
            }
            Intent intent = new Intent(CreerParcours.this, ChoixDuThemeActivity.class);
            startActivityForResult(intent, 1);
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
        if (results != null) {
            if (results.getAsJsonArray("routes") != null) {
                //Case googlemaps direction
                List<LatLng> listePoints = Tools.decodeDirections(results);
                currentLine = mMap.addPolyline(new PolylineOptions()
                        .addAll(listePoints != null ? listePoints : null));
            } else if (results.get("status") != null) {
                //Case Backend
                JsonArray resultsArray = results.getAsJsonArray("places");
                System.out.println(resultsArray);
                if (resultsArray != null) {
                    for (int i = 0; i < resultsArray.size(); i++) {
                        places.add(new Place(resultsArray.get(i).getAsJsonObject()));
                        mMap.addMarker(new MarkerOptions().position(places.get(i).getPosition()));
                    }
                }
            }
        }
    }

    public void visualize(){
        URL url = Tools.generateGoogleMapURL(markers);
        PostTask postTask = new PostTask(url.toString());
        postTask.delegate = this;
        postTask.execute();
    }

    public void getPlaces(){
        GetTask getTask = new GetTask(Config.getRequest(Config.GET_PLACES));
        getTask.delegate = this;
        getTask.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ASK_FOR_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 & grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!

                } else {

                    // permission denied, boo!
                }
                return;
            }
            case ASK_FOR_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 & grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo!
                }
                return;
            }
         }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            setResult(2);
            finish();
        }
    }
}

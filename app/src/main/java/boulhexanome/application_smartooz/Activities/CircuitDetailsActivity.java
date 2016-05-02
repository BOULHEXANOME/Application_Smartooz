package boulhexanome.application_smartooz.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import boulhexanome.application_smartooz.Model.CurrentCircuitTravel;
import boulhexanome.application_smartooz.Model.CurrentCircuitsSearch;
import boulhexanome.application_smartooz.Model.Place;

import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.Utils.LocationService;
import boulhexanome.application_smartooz.Utils.Tools;
import boulhexanome.application_smartooz.WebServices.GetTask;
import boulhexanome.application_smartooz.WebServices.PostTask;

public class CircuitDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int ASK_FOR_ACCESS_COARSE_LOCATION = 1;
    private static final int ASK_FOR_ACCESS_FINE_LOCATION = 2;
    private Polyline currentLine;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    private MapFragment mMapFragment;
    private GoogleMap mMap;

    // Interface
    private ActionBar toolbar;
    private ScrollView mScrollView;
    private LinearLayout scrollButtonLayout;
    boolean mapIsHidden;

    // Les elements qu'on doit mettre a jour
    private TextView circuitTitle, circuitInformationsTextview, circuitDescription, keywordsTextview, lengthKmTextview, heightDifferenceTextview;
    private RatingBar circuitRatingBar;
    private ListView placesList;

    // Infos issues du serveur
    private Circuit theCircuit;
    private ArrayList<Place> listOfPlaces = new ArrayList<Place>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit_details);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Détails du circuit");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        mapIsHidden = false;

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.circuit_details_map);
        mMapFragment.getMapAsync(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mScrollView = (ScrollView) findViewById(R.id.circuit_details_scrollview);

        scrollButtonLayout = (LinearLayout) findViewById(R.id.drag_button_layout);
        scrollButtonLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                if (mapIsHidden) {
                    mMapFragment.getView().setVisibility(View.GONE);
                    mScrollView.setVisibility(View.VISIBLE);
                    mapIsHidden = false;
                } else {
                    mMapFragment.getView().setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                    mapIsHidden = true;
                }

            }
        }); // Fin decla listener

        // Definition des objets graphiques que l'on va mettre a jour
        circuitTitle = (TextView) findViewById(R.id.circuit_title);
        circuitInformationsTextview = (TextView) findViewById(R.id.circuit_global_informations);
        circuitRatingBar = (RatingBar) findViewById(R.id.circuit_details_rating);
        circuitDescription = (TextView) findViewById(R.id.circuit_description);
        keywordsTextview = (TextView) findViewById(R.id.keywords_circuit_details);
        lengthKmTextview = (TextView) findViewById(R.id.length_km);
        heightDifferenceTextview = (TextView) findViewById(R.id.circuit_height_difference_m);

        placesList = (ListView)findViewById(R.id.places_list);

        // Recuperation de l'objet circuit transmis depuis la liste des circuits
        theCircuit = CurrentCircuitsSearch.getInstance().getSelectedCircuit();
        //theCircuit = (Circuit) getIntent().getSerializableExtra("Circuit");
        if (theCircuit != null) {
            // MAJ des elements de la vue pour afficher les infos dans l'objet circuit
            circuitTitle.setText(theCircuit.getName());

            // Recup les mots clefs
            String keywords = "";
            for (int i = 0; i < theCircuit.getKeywords().size(); i++) {
                keywords += theCircuit.getKeywords().get(i) + " ";
            }

            circuitInformationsTextview.setText(theCircuit.getLengthKm() + " km - " + keywords);
            circuitRatingBar.setRating(theCircuit.getNoteOn5());
            circuitDescription.setText(theCircuit.getDescription());
            keywordsTextview.setText("Mots-clefs : " + keywords);
            lengthKmTextview.setText("Distance : " + theCircuit.getLengthKm() + " km");
            heightDifferenceTextview.setText("Dénivelé : " + theCircuit.getDeniveleM() + " m");

            // Appels au Back pour recuperer les Places associees
            // Pour chaque id de place contenue dans circuit, on envoie une requete au back pour recuperer ce circuit
            //for (int i = 0; i < theCircuit.getPlaces.lenght; i++) {


            //}

            Button lancerCeParcoursButton = (Button)findViewById(R.id.lancerCeParcours);
            lancerCeParcoursButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CurrentCircuitTravel.getInstance().setCircuitEnCours(theCircuit);
                    startService(new Intent(CircuitDetailsActivity.this, LocationService.class));
                }
            });
        }


    } // Fin onCreate

    public void addPlaceInformationsToList(Place theNewPlace) {

        // Ajout d'un element a la liste des places et maj de ses infos sur la vue graphique

        //theNewPlace
        //ListView placesList


    }

    public void visualizeReceived(JsonObject results) {
        if (results != null) {
            List<LatLng> listePoints = Tools.decodeDirections(results);
            currentLine = mMap.addPolyline(new PolylineOptions()
                    .addAll(listePoints != null ? listePoints : null));
        }
    }

    public void getPlacesReceived(JsonObject results) {
        if (results != null) {
            JsonObject resultObject = results.getAsJsonObject("place");
            System.out.println("Le results array : " + resultObject);
            if (resultObject != null) {

                Place newPlace = new Place(resultObject);
                listOfPlaces.add(newPlace);

                // MAJ du circuit
                theCircuit.setPlaces(listOfPlaces);

                // MAJ graphique de la liste des places
                addPlaceInformationsToList(newPlace);

                // Calcul de la polyligne via Google Maps
                Marker newMarker = mMap.addMarker(new MarkerOptions().position(newPlace.getPosition()));
                markers.add(newMarker);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(newMarker.getPosition()));
                //newMarker.showInfoWindow();

                //Affichage dynamique du parcours
                URL url = Tools.generateGoogleMapURL(markers);
                PostTask postTask = new PostTask(url.toString());
                postTask.delegate = new HandleVisualizationDetailsCircuit(CircuitDetailsActivity.this);
                postTask.execute();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLngBounds GRAND_LYON = new LatLngBounds(
                new LatLng(45.720301, 4.779128), new LatLng(45.797678, 4.926584));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ASK_FOR_ACCESS_COARSE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ASK_FOR_ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    marker.showInfoWindow();
                    return true;

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
                    for (int i = 0; i < listOfPlaces.size(); i++) {
                        if (listOfPlaces.get(i).getPosition().equals(position)) {
                            placeMarked = listOfPlaces.get(i);
                            i = listOfPlaces.size();
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
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    tags.setText(stringBuilder.toString());

                    final Place finalPlaceMarked = placeMarked;

                    // Returning the view containing InfoWindow contents
                    return v;
                }
            });


            //Recuperation des id des places pour requetage
            ArrayList<Integer> placesId = theCircuit.getPlacesId();

            for (Integer id : placesId) {
                GetTask getTask = new GetTask(Config.getRequest(Config.GET_PLACES_ID) + "/" + id);
                getTask.delegate = new HandleGetPlacesDetailsCircuit(this);
                getTask.execute();
            }


            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(GRAND_LYON, 10));
                }
            });

        } catch (SecurityException e) {
            System.out.println(e);
        }

    }

    //Gestion des permissions : à implémenter pour le bug Mapsize !
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


}

class HandleVisualizationDetailsCircuit implements PostTask.AsyncResponse {

    private CircuitDetailsActivity circuitDetailsActivity;

    public HandleVisualizationDetailsCircuit(CircuitDetailsActivity circuitDetailsActivity) {
        this.circuitDetailsActivity = circuitDetailsActivity;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.circuitDetailsActivity.visualizeReceived(results);
    }
}

class HandleGetPlacesDetailsCircuit implements GetTask.AsyncResponse {

    private CircuitDetailsActivity circuitDetailsActivity;

    public HandleGetPlacesDetailsCircuit(CircuitDetailsActivity circuitDetailsActivity) {
        this.circuitDetailsActivity = circuitDetailsActivity;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.circuitDetailsActivity.getPlacesReceived(results);
    }
}



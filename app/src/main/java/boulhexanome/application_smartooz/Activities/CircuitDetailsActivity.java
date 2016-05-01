package boulhexanome.application_smartooz.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.Place;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.WebServices.GetTask;

public class CircuitDetailsActivity extends AppCompatActivity implements GetTask.AsyncResponse {

    // Interface
    private ActionBar toolbar;
    private MapFragment mMapFragment;
    private ScrollView mScrollView;
    private LinearLayout scrollButtonLayout;
    boolean mapIsHidden;

    // Les elements qu'on doit mettre a jour
    private TextView circuitTitle, circuitInformationsTextview, circuitDescription, timeMinutesTextview, lengthKmTextview, heightDifferenceTextview;
    private RatingBar circuitRatingBar;

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
        //mMapFragment.getMapAsync(this);

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
        timeMinutesTextview = (TextView) findViewById(R.id.time_minutes);
        lengthKmTextview = (TextView) findViewById(R.id.length_km);
        heightDifferenceTextview = (TextView) findViewById(R.id.circuit_height_difference_m);

        // Recuperation de l'objet circuit transmis depuis la liste des circuits
        theCircuit = (Circuit) getIntent().getSerializableExtra("Circuit");
        if (theCircuit != null) {
            // MAJ des elements de la vue pour afficher les infos dans l'objet circuit
            circuitTitle.setText(theCircuit.getName());

            // Recup les mots clefs
            String keywords = "";
            for (int i = 0; i < theCircuit.getKeywords().size(); i++) {
                keywords += theCircuit.getKeywords().get(i) + " ";
            }

            //circuitInformationsTextview.setText(Durée, theCircuit.getLengthKm(), keywords);
            circuitRatingBar.setRating(theCircuit.getNoteOn5());
            circuitDescription.setText(theCircuit.getDescription());
            //timeMinutesTextview.setText("Durée conseillée : " + duree + " mins");
            lengthKmTextview.setText("Distance : " + theCircuit.getLengthKm() + " km");
            heightDifferenceTextview.setText("Dénivelé : " + theCircuit.getDeniveleM() + " m");

            // Appels au Back pour recuperer les Places associees
            // Pour chaque id de place contenue dans circuit, on envoie une requete au back pour recuperer ce circuit
            //for (int i = 0; i < theCircuit.getPlaces.lenght; i++) {


            //}

        }


    } // Fin onCreate


    //Recupere le JSON un fois la requete au serveur effectuee
    @Override
    synchronized public void processFinish(JsonObject results) {

        // Les Json qui arrivent sont des places, les ajouter à la liste des places et maj la vue, cad ajouter un objet dans la liste
        if (results != null) {

            JsonObject thePlaceJson = results.getAsJsonObject("place");
            System.out.println(thePlaceJson);
            if (thePlaceJson != null) {

                Place newPlace = new Place(thePlaceJson);
                listOfPlaces.add(newPlace);
                // MAJ de la vue avec le nouveau Place
                addPlaceInformationsToList(newPlace);

            }

        }

    }

    // Papin c'est pour toi !!
    // Fais en sorte qu'on ajoute bien les places a la liste et que ça soit joli x)
    public void addPlaceInformationsToList(Place theNewPlace) {

        // Ajout d'un element a la liste des places et maj de ses infos sur la vue graphique

        //theNewPlace


    }


}


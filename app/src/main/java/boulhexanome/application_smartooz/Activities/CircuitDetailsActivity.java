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
    private TextView circuitTitle, circuitInformationsTextview, timeMinutesTextview, lengthKmTextview, heightDifferenceTextview;
    private RatingBar circuitRatingBar;

    // Infos issues du serveur
    private Circuit theCircuit;
    private ArrayList<Place> listOfPlaces;



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

            if(mapIsHidden)
            {
                mMapFragment.getView().setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
                mapIsHidden = false;
            }
            else
            {
                mMapFragment.getView().setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
                mapIsHidden = true;
            }

        }
    }); // Fin decla listener


        // Definition des objets que l'on va mettre a jour
        circuitTitle = (TextView) findViewById(R.id.circuit_title);
        circuitInformationsTextview = (TextView) findViewById(R.id.circuit_global_informations);
        circuitRatingBar = (RatingBar) findViewById(R.id.circuit_details_rating);
        timeMinutesTextview = (TextView) findViewById(R.id.time_minutes);
        lengthKmTextview = (TextView) findViewById(R.id.length_km);
        heightDifferenceTextview = (TextView) findViewById(R.id.circuit_height_difference_m);



        // Appels au Back pour remplir les objets Circuit et les Places associees



        // MAJ des elements de la vue pour afficher les infos recuperees
        //circuitTitle.setText(LeTitre);
        //circuitInformationsTextview.setText(Durée, Kms, Mot-clefs);
        //circuitRatingBar.setRating(leRating);
        //timeMinutesTextview.setText(" Durée conseillée : " + duree + " mins");
        //lengthKmTextview.setText("Distance : " + distance_km + " km");
        //heightDifferenceTextview.setText("Dénivelé : " + denivele + " m");

    } // Fin onCreate


    //Recupere le JSON un fois la requete au serveur effectuee
    @Override
    public void processFinish(JsonObject results) {

        // Si le json qui arrive est un circuit
        // Le convertir en objet Circuit et lancer autant de get task qu'il y a de places associees

        // Si le json qui arrive est une place
        // Le convertir en objet Place et l'ajouter à la liste des places



        // Mettre a jour la vue ICI ?

    }



}


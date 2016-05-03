package boulhexanome.application_smartooz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.JsonObject;

import boulhexanome.application_smartooz.Model.CurrentCircuitTravel;
import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.Utils.LocationService;
import boulhexanome.application_smartooz.WebServices.PostTask;

import static android.widget.RatingBar.*;

public class CongratulationsCircuitEndActivity extends AppCompatActivity implements PostTask.AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_congratulations_circuit_end);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);


        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        if (ratingBar != null) {
            ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    System.out.println(rating);
                    handleVote(rating, fromUser);
                }
            });
        }

        final Button finirParcours = (Button) findViewById(R.id.finirParcours);
        finirParcours.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finirParcoursClicked();
            }
        });

        int placeId = CurrentCircuitTravel.getInstance().getCircuitEnCours().getId();

        PostTask inscription_thread = new PostTask(Config.getRequest(Config.CIRCUIT_DONE));
        inscription_thread.delegate = new HandleCircuitDone(this);

        JsonObject vote = new JsonObject();
        vote.addProperty("id", placeId);

        inscription_thread.execute(vote);

    }

    private void finirParcoursClicked(){
        CurrentCircuitTravel.getInstance().toDelete = true;
        Intent intent = new Intent(CongratulationsCircuitEndActivity.this, PlaceNearbyActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void handleVote(float rating, boolean fromUser){
        int placeId = CurrentCircuitTravel.getInstance().getCircuitEnCours().getId();

        PostTask inscription_thread = new PostTask(Config.getRequest(Config.VOTE_CIRCUIT));
        inscription_thread.delegate = this;

        JsonObject vote = new JsonObject();
        vote.addProperty("id", placeId);
        vote.addProperty("note", rating);

        inscription_thread.execute(vote);
    }

    @Override
    public void processFinish(JsonObject results) {
        if (results.get("status").getAsString().equals("OK")) {
            Toast.makeText(CongratulationsCircuitEndActivity.this, "Vote reçu.", Toast.LENGTH_SHORT).show();
        } else if (results.get("status").getAsString().equals("KO")) {
            Toast.makeText(CongratulationsCircuitEndActivity.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CongratulationsCircuitEndActivity.this, "Erreur connexion serveur", Toast.LENGTH_SHORT).show();
        }
    }

    public void circuitDoneReceived(JsonObject results) {
        if (results.get("status").getAsString().equals("KO")) {
            Toast.makeText(CongratulationsCircuitEndActivity.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CongratulationsCircuitEndActivity.this, "Erreur connexion serveur", Toast.LENGTH_SHORT).show();
        }
    }
}


class HandleCircuitDone implements PostTask.AsyncResponse{
    private CongratulationsCircuitEndActivity congratulationsCircuitEndActivity;

    public HandleCircuitDone(CongratulationsCircuitEndActivity congratulationsCircuitEndActivity) {
        this.congratulationsCircuitEndActivity = congratulationsCircuitEndActivity;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.congratulationsCircuitEndActivity.circuitDoneReceived(results);
    }
}
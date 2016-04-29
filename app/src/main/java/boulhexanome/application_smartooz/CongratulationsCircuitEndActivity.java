package boulhexanome.application_smartooz;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.JsonObject;

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
        // TODO change !!! Get real id
        int userId = 1;

        PostTask inscription_thread = new PostTask(Config.getRequest(Config.VOTE_CIRCUIT));
        inscription_thread.delegate = this;

        JsonObject vote = new JsonObject();
        vote.addProperty("id", userId);
        vote.addProperty("note", rating);

        inscription_thread.execute(vote);
    }

    @Override
    public void processFinish(JsonObject results) {
        System.out.println(results.toString());
        if (results.get("status").getAsString().equals("OK")) {
            Toast.makeText(CongratulationsCircuitEndActivity.this, "Vote reçu.", Toast.LENGTH_SHORT).show();
        } else if (results.get("status").getAsString().equals("KO")) {
            Toast.makeText(CongratulationsCircuitEndActivity.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CongratulationsCircuitEndActivity.this, "BUG", Toast.LENGTH_SHORT).show();
        }
    }
}

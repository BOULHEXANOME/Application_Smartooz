package boulhexanome.application_smartooz.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.CurrentPlaceDetail;
import boulhexanome.application_smartooz.Model.Place;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.WebServices.GetTask;
import boulhexanome.application_smartooz.WebServices.PostTask;

public class DetailParcoursPlace extends AppCompatActivity {
    private TextView textViewDescription;
    private TextView textViewSubtitle;
    private TextView textViewTitle;
    private TextView textViewNumTel;
    private TextView textViewWebSite;
    private TextView textViewAddress;
    private ImageView imageView;
    private RatingBar ratingBar;
    private ActionBar toolbar;
    private Circuit circuit;
    private Place place;
    String API_KEY = "AIzaSyDWlPi3Sbzq33C6yK-dem9XPga0E9a402U";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_parcours_place);
        textViewDescription = (TextView)findViewById(R.id.textViewDescriptionPlace);
        textViewTitle = (TextView)findViewById(R.id.textViewTitlePlace);
        textViewSubtitle = (TextView)findViewById(R.id.textViewSubtitlePlace);
        textViewNumTel = (TextView)findViewById(R.id.textViewNumTel);
        textViewWebSite = (TextView)findViewById(R.id.textViewWebSite);
        textViewAddress = (TextView)findViewById(R.id.textViewAddress);
        ratingBar = (RatingBar)findViewById(R.id.ratingBarPN);
        textViewDescription.setVerticalScrollBarEnabled(true);
        imageView = (ImageView)findViewById(R.id.imageViewPlace);
        RelativeLayout RL = (RelativeLayout)findViewById(R.id.relativeLayoutPN);
        RL.setBackgroundColor(Color.LTGRAY);
        LinearLayout LL = (LinearLayout) findViewById(R.id.linearLayoutPN);
        LL.setBackgroundResource(R.drawable.rectangle);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Détails du Lieu");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        place = CurrentPlaceDetail.getInstance().getPlaceEnCours();
        String nom = place.getName();
        nom+="+lyon";
        String keywords = "";
        try {
            keywords = URLEncoder.encode(nom, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Querying(keywords);
        } catch (IOException e) {
            e.printStackTrace();
            textViewDescription.append("nike sa m...");
        }

        final FloatingActionButton add = (FloatingActionButton) findViewById(R.id.action_add_photo_place);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int TAKE_PHOTO_CODE = 0;
                String file = "hola.jpg";
                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                }
                catch (IOException e)
                {
                }
                Uri outputFileUri = Uri.fromFile(newfile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });

        RatingBar rating = (RatingBar) findViewById(R.id.ratingBarPN);
        if (ratingBar != null) {
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    System.out.println(rating);
                    handleVote(rating);
                }
            });
        }
    }

    protected void handleVote(float rating){
        int placeId = CurrentPlaceDetail.getInstance().getPlaceEnCours().getId();

        PostTask inscription_thread = new PostTask(Config.getRequest(Config.VOTE_PLACE));
        inscription_thread.delegate = new HandleNoteReceived2(this);

        JsonObject vote = new JsonObject();
        vote.addProperty("id", placeId);
        vote.addProperty("note", rating);

        inscription_thread.execute(vote);
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

    public void Querying (String parameters) throws IOException {
        GetTask getPlaceNearby = new GetTask("https://kgsearch.googleapis.com/v1/entities:search?query="+parameters+"&key="+API_KEY+"&limit=1&indent=True&languages=fr");
        getPlaceNearby.delegate = new HandleGetPlaceNearbyResponse2(this);
        JsonObject j = new JsonObject();
        getPlaceNearby.execute(j);
    }

    public void placeReceived(JsonObject results){
        JsonArray item = results.get("itemListElement").getAsJsonArray();

        JsonElement name = null;
        JsonElement descriptionCourte = null;
        JsonElement imageURL = null;
        JsonElement description = null;
        try{
            JsonElement element = item.get(0);
            JsonObject object = element.getAsJsonObject().get("result").getAsJsonObject();
            name = object.get("name");
            descriptionCourte = object.get("description");
            JsonObject imagesURL = object.get("image").getAsJsonObject();
            imageURL = imagesURL.get("contentUrl");
            JsonObject descriptionLongue = object.get("detailedDescription").getAsJsonObject();
            description = descriptionLongue.get("articleBody");
        }catch (Exception e){

        }

        textViewTitle.setText("");
        if(name !=null) {
            textViewTitle.append(name.getAsString());
        }else if(place.getName()!=null){
            textViewTitle.append(place.getName());
        }

        textViewSubtitle.setText("");
        if(descriptionCourte!=null) {
            textViewSubtitle.append(descriptionCourte.getAsString());
        }else{
            for(int i = 0; i<place.getKeywords().size(); i++) {
                textViewSubtitle.append(place.getKeywords().get(i));
                textViewSubtitle.append(" ");
            }
        }

        textViewDescription.setText("");
        if(description!=null) {
            textViewDescription.append(description.getAsString());
        }else if(place.getDescription()!=null){
            textViewDescription.append(place.getDescription());
        }

        if(imageURL !=null){
            new DownloadImageTask(imageView).execute(imageURL.getAsString());
        }else if(place.getUrlImage()!=null){
            new DownloadImageTask(imageView).execute(place.getUrlImage());
        }

        textViewNumTel.setText("");
        if(place.getPhone()!=null){
            textViewNumTel.setVisibility(View.VISIBLE);
            textViewNumTel.setText("Numéro téléphone : "+place.getPhone());
        }else{
            textViewNumTel.setVisibility(View.GONE);
        }

        textViewAddress.setText("");
        if(place.getAddress()!=null){
            textViewAddress.setVisibility(View.VISIBLE);
            textViewAddress.setText("Adresse : "+place.getAddress());
        }else{
            textViewAddress.setVisibility(View.GONE);
        }

        textViewWebSite.setText("");
        if(place.getWebsite()!=null){
            textViewWebSite.setVisibility(View.VISIBLE);
            textViewWebSite.setText("Site Web : "+place.getWebsite());
        }else{
            textViewWebSite.setVisibility(View.GONE);
        }

        ratingBar.setRating(place.getNoteOn5());
    }

    public void noteReceived(JsonObject results) {
        if (results.get("status").getAsString().equals("KO")) {
            Toast.makeText(DetailParcoursPlace.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DetailParcoursPlace.this, "Erreur connexion serveur", Toast.LENGTH_SHORT).show();
        }
    }
}
class HandleNoteReceived2 implements PostTask.AsyncResponse{
    private DetailParcoursPlace detailParcoursPlace;

    public HandleNoteReceived2(DetailParcoursPlace detailParcoursPlace) {
        this.detailParcoursPlace = detailParcoursPlace;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.detailParcoursPlace.noteReceived(results);
    }
}
class HandleGetPlaceNearbyResponse2 implements GetTask.AsyncResponse{

    private DetailParcoursPlace detailParcoursPlace;

    public HandleGetPlaceNearbyResponse2(DetailParcoursPlace detailParcoursPlace) {
        this.detailParcoursPlace = detailParcoursPlace;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.detailParcoursPlace.placeReceived(results);
    }
}
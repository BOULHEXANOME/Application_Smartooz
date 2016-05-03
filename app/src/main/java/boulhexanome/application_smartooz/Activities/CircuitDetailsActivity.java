package boulhexanome.application_smartooz.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;


import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
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
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.CurrentCircuitDetail;
import boulhexanome.application_smartooz.Model.CurrentCircuitTravel;
import boulhexanome.application_smartooz.Model.CurrentCircuitsSearch;
import boulhexanome.application_smartooz.Model.Place;

import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.Utils.LocationService;
import boulhexanome.application_smartooz.Utils.Tools;
import boulhexanome.application_smartooz.WebServices.GetTask;
import boulhexanome.application_smartooz.WebServices.PostTask;

public class CircuitDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private boolean not_first_time_showing_info_window;
    private static final int ASK_FOR_ACCESS_COARSE_LOCATION = 1;
    private static final int ASK_FOR_ACCESS_FINE_LOCATION = 2;
    private Polyline currentLine;
    ArrayList<Marker> markers = new ArrayList<>();
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private int numberOfReceivedPlaces;
    private Uri fileUri;

    // Interface
    private ActionBar toolbar;
    private ScrollView mScrollView;
    private LinearLayout scrollButtonLayout;
    boolean mapIsHidden;

    // Les elements qu'on doit mettre a jour
    private TextView circuitTitle, circuitInformationsTextview, circuitDescription, keywordsTextview, lengthKmTextview, heightDifferenceTextview;
    private RatingBar circuitRatingBar;
    private RatingBar votingBar;
    private ListView placesList;

    // Infos issues du serveur
    private Circuit theCircuit;
    private ArrayList<Place> listOfPlaces = new ArrayList<Place>();
    private boolean parcoursEstLance = false;
    private Uri selectedImage;
    private Bitmap photo;
    private String picturePath;
    private String ba1;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Pour ne lancer la requete Gmap que quand on a recupere toutes les places
        numberOfReceivedPlaces = 0;

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
                    mMapFragment.getView().setVisibility(View.VISIBLE);
                    mScrollView.setVisibility(View.GONE);
                    mapIsHidden = false;
                } else {
                    mMapFragment.getView().setVisibility(View.GONE);
                    mScrollView.setVisibility(View.VISIBLE);
                    mapIsHidden = true;
                }

            }
        }); // Fin decla listener

        // Definition des objets graphiques que l'on va mettre a jour
        circuitTitle = (TextView) findViewById(R.id.circuit_title);
        circuitInformationsTextview = (TextView) findViewById(R.id.circuit_global_informations);
        circuitRatingBar = (RatingBar) findViewById(R.id.circuit_details_rating);
        votingBar = (RatingBar) findViewById(R.id.voting_bar);
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

            Button lancerCeParcoursButton = (Button)findViewById(R.id.lancerCeParcours);
            lancerCeParcoursButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickLancerParcours();
                }
            });

            final FloatingActionButton add = (FloatingActionButton) findViewById(R.id.action_add_photo);
            assert add != null;
            add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    int TAKE_PHOTO_CODE = 0;
//                    String file = "hola.jpg";
//                    File newfile = new File(file);
//                    try {
//                        newfile.createNewFile();
//                    }
//                    catch (IOException e)
//                    {
//                    }
//                    Uri outputFileUri = Uri.fromFile(newfile);
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

//                    startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

                    if (getApplicationContext().getPackageManager().hasSystemFeature(
                            PackageManager.FEATURE_CAMERA)) {
                        // Open default camera
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        startActivityForResult(intent, 100);

                    } else {
                        Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                    }

                    url = Config.getRequest(Config.UPLOAD_IMAGE_CIRCUIT + Integer.toString(theCircuit.getId()));

                    //postTask.execute(newfile);
                }
            });

            /*
            if (votingBar != null) {
                //System.out.print("Voting Bar listener");

                votingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        System.out.println(rating);
                        postVote(rating, fromUser);
                    }
                });
            } */

            votingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        float touchPositionX = event.getX();
                        float width = votingBar.getWidth();
                        float rating = (touchPositionX / width) * 5.0f;
                        //int stars = (int)starsf + 1;
                        votingBar.setRating(rating);
                        //Toast.makeText(CircuitDetailsActivity.this, String.valueOf("test"), Toast.LENGTH_SHORT).show();
                        postVote(rating);
                        v.setPressed(false);

                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setPressed(true);
                    }

                    if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        v.setPressed(false);
                    }
                    return true;
                }});


        }

        // Demarrage sur la map directement
        mMapFragment.getView().setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);

    } // Fin onCreate


    protected void postVote(float rating) {
        int placeId = theCircuit.getId();

        PostTask voteTask = new PostTask(Config.getRequest(Config.VOTE_CIRCUIT));
        voteTask.delegate = new HandleCircuitVote(CircuitDetailsActivity.this);

        JsonObject vote = new JsonObject();
        vote.addProperty("id", placeId);
        vote.addProperty("note", rating);
        //System.out.println("Vote : " + placeId + ", note : " + rating);

        voteTask.execute(vote);
    }

    public void clickLancerParcours(){
        Button lancerCeParcoursButton = (Button) findViewById(R.id.lancerCeParcours);
        if(!this.parcoursEstLance){
            this.parcoursEstLance = true;
            CurrentCircuitTravel.getInstance().setCircuitEnCours(theCircuit);
            startService(new Intent(CircuitDetailsActivity.this, LocationService.class));
            lancerCeParcoursButton.setText("Arrêter ce parcours");

            mMapFragment.getView().setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.GONE);
            mapIsHidden = true;
        }else{
            this.parcoursEstLance = false;
            stopService(new Intent(CircuitDetailsActivity.this, LocationService.class));
            CurrentCircuitTravel.getInstance().setCircuitEnCours(null);
            lancerCeParcoursButton.setText(R.string.lancer_ce_parcours);
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

    public void refreshPlacesList() {

        // Ajout d'un element a la liste des places et maj de ses infos sur la vue graphique
        ArrayList<String> placesListString = new ArrayList<String>();

        for (Place place : listOfPlaces) {

            placesListString.add(place.getName());
        }
        final ListAdapter adapter = new ArrayAdapter<>(CircuitDetailsActivity.this, android.R.layout.simple_list_item_1, placesListString);
        placesList.setAdapter(adapter);

        // Set le listener pour afficher sur la carte quand on clique sur la liste
        placesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //System.out.println("Position : " + position);

                // Montrer la map
                mMapFragment.getView().setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
                mapIsHidden = true;

                mMap.animateCamera(CameraUpdateFactory.newLatLng(markers.get(position).getPosition()));
                markers.get(position).showInfoWindow();

            }
        });

        placesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentCircuitDetail.getInstance().setCircuitEnCours(theCircuit);
                CurrentCircuitDetail.getInstance().setPlaceIndex(position);
                Intent myIntent = new Intent(CircuitDetailsActivity.this, DetailParcoursPlace.class);
                CircuitDetailsActivity.this.startActivity(myIntent);
                // Lancer PlaceNearbyActivity : 

                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                return true;

            }
        });



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
           //System.out.println("Le results array : " + resultObject);
            if (resultObject != null) {

                Place newPlace = new Place(resultObject);
                listOfPlaces.add(newPlace);
                numberOfReceivedPlaces++;

                Marker newMarker = mMap.addMarker(new MarkerOptions().position(newPlace.getPosition()));
                markers.add(newMarker);

                //mMap.animateCamera(CameraUpdateFactory.newLatLng(newMarker.getPosition()));


                if (numberOfReceivedPlaces == theCircuit.getPlacesId().size()) {

                    // MAJ du circuit
                    theCircuit.setPlaces(listOfPlaces);

                    // MAJ graphique de la liste des places
                    refreshPlacesList();

                    // Calcul de la polyligne via Google Maps
                    /*
                    Marker newMarker = mMap.addMarker(new MarkerOptions().position(newPlace.getPosition()));
                    markers.add(newMarker);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(newMarker.getPosition())); */
                    //newMarker.showInfoWindow();

                    //Affichage dynamique du parcours
                    URL url = Tools.generateGoogleMapURL(markers);
                    PostTask postTask = new PostTask(url.toString());
                    postTask.delegate = new HandleVisualizationDetailsCircuit(CircuitDetailsActivity.this);
                    postTask.execute();

                }

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

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
                    not_first_time_showing_info_window = false;
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
                public View getInfoContents(Marker arg0) {

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
                    RatingBar noteOn5 = (RatingBar) v.findViewById(R.id.noteOn5);
                    TextView tags = (TextView) v.findViewById(R.id.tags_infowindow);
                    TextView numero = (TextView) v.findViewById(R.id.numero_place);
                    ImageView image = (ImageView) v.findViewById(R.id.imagePi);
                    title.setText(placeMarked.getName());
                    description.setText(placeMarked.getDescription());
                    noteOn5.setRating(Float.valueOf(placeMarked.getNoteOn5()));

                    String numeroListe ="";
                    for (int i = 0; i < markers.size(); i++) {
                        if (placeMarked.getPosition().equals(markers.get(i).getPosition())){
                            numeroListe = numeroListe + String.valueOf(i+1) + "&";
                        }
                    }

                    if (numeroListe.endsWith("&")){
                        numero.setHeight(75);
                        numeroListe = numeroListe.substring(0,numeroListe.length()-1);
                        numero.setText("Etape " + numeroListe);
                    } else {
                        numero.setHeight(0);
                        numero.setText("");
                    }

                    if(placeMarked.getUrlImage()!=null){
                        if(not_first_time_showing_info_window){
                            Picasso.with(CircuitDetailsActivity.this).load(placeMarked.getUrlImage()).into(image);
                        }else{
                            not_first_time_showing_info_window = true;
                            Picasso.with(CircuitDetailsActivity.this).load(placeMarked.getUrlImage()).into(image, new InfoWindowRefresher(arg0));
                        }
                    }

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

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {

            if (Build.VERSION.SDK_INT < 19) {
                selectedImage = data.getData();
                photo = (Bitmap) data.getExtras().get("data");

                // Cursor to get image uri to display

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();

                //            Bitmap photo = (Bitmap) data.getExtras().get("data");
                //            ImageView imageView = (ImageView) findViewById(R.id.Imageprev);
                //            imageView.setImageBitmap(photo);


                Bitmap bm = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                byte[] ba = bao.toByteArray();
                //            ba1 = Base64.encodeBase64(ba);
                ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

            }else{
                Bitmap bm = (Bitmap)data.getExtras().get("data");
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
                byte[] ba = bao.toByteArray();
                //            ba1 = Base64.encodeBase64(ba);
                ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
            }
            (new UploadToServer(this, url, ba1)).execute();

        }
    }


    public void handleRating(JsonObject results) {
        if (results != null) {

            if (results.get("status").getAsString().equals("OK")) {
                Toast.makeText(CircuitDetailsActivity.this, "Vote reçu.", Toast.LENGTH_SHORT).show();
            } else if (results.get("status").getAsString().equals("KO")) {
                Toast.makeText(CircuitDetailsActivity.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CircuitDetailsActivity.this, "Erreur connexion serveur", Toast.LENGTH_SHORT).show();
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

class HandleCircuitVote implements PostTask.AsyncResponse{

    private CircuitDetailsActivity circuitDetailsActivity;

    public HandleCircuitVote(CircuitDetailsActivity circuitDetailsActivity) {
        this.circuitDetailsActivity = circuitDetailsActivity;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.circuitDetailsActivity.handleRating(results);
    }
}

class UploadToServer extends AsyncTask<Void, Void, String> {

    private final String url;
    private final String base64;
    private ProgressDialog pd;
    protected void onPreExecute() {
        super.onPreExecute();
        pd.setMessage("Wait image uploading!");
        pd.show();
    }

    public UploadToServer(CircuitDetailsActivity circuitDetailsActivity, String url, String base64){
        pd = new ProgressDialog(circuitDetailsActivity);
        this.url = url;
        this.base64 = base64;
    }

    @Override
    protected String doInBackground(Void... params) {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("base64", base64));
        nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            if(User.getInstance().getCookieManager().getCookieStore().getCookies().size() > 0)
            {
                httppost.setHeader("Cookie",
                        TextUtils.join(";",  User.getInstance().getCookieManager().getCookieStore().getCookies()));
            }

            HttpResponse response = httpclient.execute(httppost);
            String st = EntityUtils.toString(response.getEntity());
            Log.v("log_tag", "In the try Loop" + st);

        } catch (Exception e) {
            Log.v("log_tag", "Error in http connection " + e.toString());
        }
        return "Success";

    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        pd.hide();
        pd.dismiss();
    }
}

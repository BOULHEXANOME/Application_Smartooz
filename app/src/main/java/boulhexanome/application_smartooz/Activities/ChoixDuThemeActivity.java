package boulhexanome.application_smartooz.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.CurrentCircuitsSearch;
import boulhexanome.application_smartooz.Model.Place;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.WebServices.GetTask;
import boulhexanome.application_smartooz.WebServices.PostTask;

public class ChoixDuThemeActivity extends AppCompatActivity implements PostTask.AsyncResponse{

    private List<String> motsClefs;
    private ActionBar toolbar;
    private List<String> motsSelectionnes;
    private Circuit circuit;
    private Uri fileUri;
    private Circuit theCircuit;
    private ArrayList<Place> listOfPlaces = new ArrayList<Place>();
    private boolean parcoursEstLance = false;
    private Uri selectedImage;
    private Bitmap photo;
    private String picturePath;
    private String ba1;
    private String url;

    protected void hideKeyboard(int layout) {
        findViewById(layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_du_theme);

        circuit = (Circuit) getIntent().getSerializableExtra("Circuit");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Choix des informations");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        hideKeyboard(R.id.choix_layout1);
        hideKeyboard(R.id.choix_layout2);
        hideKeyboard(R.id.choix_layout3);
        hideKeyboard(R.id.choix_layout4);

        final EditText edittext = (EditText)findViewById(R.id.motsClefsRech_editText);
        assert edittext != null;

        motsSelectionnes = new ArrayList<>();
        motsClefs = new ArrayList<>();

        final ListView listMotsProposes = (ListView) findViewById(R.id.motsClefsTh_listView);
        final ListView listMotsChoisis = (ListView) findViewById(R.id.motsChoisisTh_listView);
        final ListAdapter adapt = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsClefs);
        final ListAdapter adaptChoisi = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);

        assert listMotsProposes != null;
        listMotsProposes.setAdapter(adapt);
        listMotsChoisis.setAdapter(adaptChoisi);

        listMotsProposes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String texte = (String) listMotsProposes.getItemAtPosition(position);

                if(!motsSelectionnes.contains(texte))
                    motsSelectionnes.add(texte);
                motsClefs.remove(texte);

                if(edittext.getText().toString().length() > 0) {
                    String searchString = edittext.getText().toString().toUpperCase();
                    List<String> newMotsClefs = new ArrayList<String>();
                    for(String mot:motsClefs) {
                        if(mot.contains(searchString) && !newMotsClefs.contains(mot)) {
                            newMotsClefs.add(mot);
                        }
                    }

                    ListAdapter newAdapt = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
                    listMotsProposes.setAdapter(newAdapt);

                } else {
                    ListAdapter newAdaptProposes = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                    listMotsProposes.setAdapter(newAdaptProposes);
                }

                ListAdapter newAdaptChoisi = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);
                listMotsChoisis.setAdapter(newAdaptChoisi);

            }
        });

        listMotsProposes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listMotsChoisis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String texte = (String) listMotsChoisis.getItemAtPosition(position);

                motsSelectionnes.remove(texte);
                if(!motsClefs.contains(texte))
                    motsClefs.add(texte);

                if(edittext.getText().toString().length() > 0) {
                    String searchString = edittext.getText().toString().toUpperCase();
                    List<String> newMotsClefs = new ArrayList<>();
                    for (String mot : motsClefs) {
                        if (mot.contains(searchString) && !newMotsClefs.contains(mot)) {
                            newMotsClefs.add(mot);
                        }
                    }

                    ListAdapter newAdapt = new ArrayAdapter<>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
                    listMotsProposes.setAdapter(newAdapt);
                } else {
                    ListAdapter newAdaptProposes = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                    listMotsProposes.setAdapter(newAdaptProposes);
                }

                ListAdapter newAdaptChoisis = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);
                listMotsChoisis.setAdapter(newAdaptChoisis);
            }
        });

        listMotsChoisis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = edittext.getText().toString().toUpperCase();
                List<String> newMotsClefs = new ArrayList<>();

                if(searchString.length() > 0 && !motsClefs.contains(searchString) && !motsSelectionnes.contains(searchString) && !newMotsClefs.contains(searchString)) {
                    newMotsClefs.add(searchString);
                }

                for(String mot:motsClefs) {
                    if(mot.contains(searchString) && !newMotsClefs.contains(mot)) {
                        newMotsClefs.add(mot);
                    }
                }

                ListAdapter newAdapt = new ArrayAdapter<>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
                listMotsProposes.setAdapter(newAdapt);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        Button bouton_creer = (Button) findViewById(R.id.bouton_creer_parcours);
        if (bouton_creer != null) {
            bouton_creer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    circuit = User.getInstance().getCircuit_en_creation();

                    circuit.setKeywords((ArrayList<String>) motsSelectionnes);
                    circuit.setName(((EditText) findViewById(R.id.nomParcours_editText)).getText().toString());
                    circuit.setDescription(((EditText) findViewById(R.id.description_editText)).getText().toString());

                    addCircuit();
                }
            });
        }

        final FloatingActionButton add = (FloatingActionButton) findViewById(R.id.action_add_photo_choix);
        assert add != null;
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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

                url = Config.getRequest(Config.UPLOAD_IMAGE_CIRCUIT);
            }
        });


        GetTask getKeywordsThread = new GetTask(Config.getRequest(Config.GET_KEYWORDS_OF_CIRCUIT));
        getKeywordsThread.delegate = new HandleGetKeywordsResponseAddCircuit(this);
        getKeywordsThread.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    public void addCircuit() {
        PostTask postTask = new PostTask(Config.getRequest(Config.ADD_CIRCUIT));
        postTask.delegate = this;

        String name = circuit.getName();
        String description = circuit.getDescription();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("description", description);

        JsonArray keywords_array = new JsonArray();
        for (int i = 0; i < circuit.getKeywords().size(); i++) {
            keywords_array.add(circuit.getKeywords().get(i));
        }
        jsonObject.add("keywords", keywords_array);

        JsonArray places_array = new JsonArray();
        for (int i = 0; i < circuit.getPlaces().size(); i++) {
            places_array.add(circuit.getPlaces().get(i).getId());
        }
        jsonObject.add("places", places_array);

        System.out.println(jsonObject);

        postTask.execute(jsonObject);
    }

    @Override
    public void processFinish(JsonObject results) {
        System.out.println(results);
        if (results != null) {
            if (results.get("status").getAsString().equals("OK")) {
                Toast.makeText(ChoixDuThemeActivity.this, "Le parcours a bien été ajouté ! ", Toast.LENGTH_SHORT).show();
                User.getInstance().setCircuit_en_creation(new Circuit());
                this.setResult(2);
                finish();
            } else if (results.get("status").getAsString().equals("KO")) {
                Toast.makeText(ChoixDuThemeActivity.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChoixDuThemeActivity.this, "BUG", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {

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

            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            ba1 = Arrays.toString(Base64.encodeBase64(ba));

            Log.e("base64", "-----" + ba1);

            // Upload image to server
            new UploadToServerChoix(this, url, ba1).execute();

        }
    }

    public void keywordsReceived(JsonObject results){
        if (results != null && results.get("status").getAsString().equals("OK")) {
            JsonArray keywords = (JsonArray)results.get("keywords");
            for(JsonElement k: keywords){
                String kName = ((JsonObject)k).get("name").getAsString();
                if(!motsClefs.contains(kName))
                    motsClefs.add(kName);
                ListView listMotsProposes = (ListView) findViewById(R.id.motsClefsTh_listView);
                ListAdapter newAdapt = new ArrayAdapter<>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                listMotsProposes.setAdapter(newAdapt);
            }
        }
    }
}


class HandleGetKeywordsResponseAddCircuit implements GetTask.AsyncResponse{

    private ChoixDuThemeActivity choixDuThemeActivity;

    public HandleGetKeywordsResponseAddCircuit(ChoixDuThemeActivity choixDuThemeActivity) {
        this.choixDuThemeActivity = choixDuThemeActivity;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.choixDuThemeActivity.keywordsReceived(results);
    }
}

class UploadToServerChoix extends AsyncTask<Void, Void, String> {

    private final String url;
    private final String base64;
    private ProgressDialog pd;
    protected void onPreExecute() {
        super.onPreExecute();
        pd.setMessage("Wait image uploading!");
        pd.show();
    }

    public UploadToServerChoix(ChoixDuThemeActivity choixDuThemeActivity, String url, String base64){
        pd = new ProgressDialog(choixDuThemeActivity);
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
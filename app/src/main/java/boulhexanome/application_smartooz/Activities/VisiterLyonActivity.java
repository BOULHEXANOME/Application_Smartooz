package boulhexanome.application_smartooz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.CurrentCircuits;
import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.RangeSeekBar;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.WebServices.GetTask;
import boulhexanome.application_smartooz.WebServices.PostTask;

public class VisiterLyonActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PostTask.AsyncResponse {

    private List<Tuple<String, Integer>> motsSelectionnes;
    private List<Tuple<String, Integer>> motsClefs;
    private RangeSeekBar<Integer> rangeSeekBar;

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
        setContentView(R.layout.activity_visiter_lyon);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        
        hideKeyboard(R.id.visiter_layout1);
        hideKeyboard(R.id.visiter_layout2);
        hideKeyboard(R.id.visiter_layout3);
        hideKeyboard(R.id.visiter_layout4);

        Button pageSuivante = (Button) findViewById(R.id.pageSuivante_button);
        pageSuivante.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                callNextPage();
            }
        });

        // Range seekbar
        rangeSeekBar = new RangeSeekBar<Integer>(this);
        rangeSeekBar.setRangeValues(0, 100);
        rangeSeekBar.setSelectedMinValue(20);
        rangeSeekBar.setSelectedMaxValue(50);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutrangebar);
        assert layout != null;
        layout.addView(rangeSeekBar);

        // Texte de la range seekbar
        final TextView textSeekBar = (TextView) findViewById(R.id.textSeekBar);
        double min = Math.floor((double) rangeSeekBar.getSelectedMinValue()/10*100)/100;
        double max = Math.floor((double) rangeSeekBar.getSelectedMaxValue()/10*100)/100;
        textSeekBar.setText("De "+ min +" à "+ max +" km.");

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                double minVal = Math.floor((double) rangeSeekBar.getSelectedMinValue()/10*100)/100;
                double maxVal = Math.floor((double) rangeSeekBar.getSelectedMaxValue()/10*100)/100;
                textSeekBar.setText("De "+ minVal +" à "+ maxVal +" km.");
            }
        });

        final EditText edittext = (EditText)findViewById(R.id.rech_editText);
        assert edittext != null;

        motsSelectionnes = new ArrayList<>();
        motsClefs = new ArrayList<>();

        final ListView listMotsProposes = (ListView) findViewById(R.id.motsClefs_listView);
        final ListView listMotsChoisis = (ListView) findViewById(R.id.motsChoisis_listView);
        final ListAdapter adapt = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsClefs);
        final ListAdapter adaptChoisi = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);

        listMotsProposes.setAdapter(adapt);
        listMotsChoisis.setAdapter(adaptChoisi);

        listMotsProposes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tuple<String, Integer> keyword = (Tuple<String, Integer>) listMotsProposes.getItemAtPosition(position);

                if(!motsSelectionnes.contains(keyword))
                    motsSelectionnes.add(keyword);
                motsClefs.remove(keyword);

                if(edittext.getText().toString().length() > 0) {
                    String searchString = edittext.getText().toString().toUpperCase();
                    List<Tuple<String, Integer>> newMotsClefs = new ArrayList<>();
                    for(Tuple<String, Integer> mot:motsClefs) {
                        if(mot.x.contains(searchString) && !newMotsClefs.contains(mot)) {
                            newMotsClefs.add(mot);
                        }
                    }

                    ListAdapter newAdapt = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
                    listMotsProposes.setAdapter(newAdapt);

                } else {
                    ListAdapter newAdaptProposes = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                    listMotsProposes.setAdapter(newAdaptProposes);
                }

                ListAdapter newAdaptChoisi = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);
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
                Tuple<String, Integer> keyword = (Tuple<String, Integer>) listMotsChoisis.getItemAtPosition(position);

                motsSelectionnes.remove(keyword);
                if(!motsClefs.contains(keyword))
                    motsClefs.add(keyword);

                if(edittext.getText().toString().length() > 0) {
                    String searchString = edittext.getText().toString().toUpperCase();
                    List<Tuple<String, Integer>> newMotsClefs = new ArrayList<>();
                    for (Tuple<String, Integer> mot : motsClefs) {
                        if (mot.x.contains(searchString) && !newMotsClefs.contains(mot)) {
                            newMotsClefs.add(mot);
                        }
                    }

                    ListAdapter newAdapt = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
                    listMotsProposes.setAdapter(newAdapt);
                } else {
                    ListAdapter newAdaptProposes = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                    listMotsProposes.setAdapter(newAdaptProposes);
                }

                ListAdapter newAdaptChoisis = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);
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
                List<Tuple<String, Integer>> newMotsClefs = new ArrayList<>();

                for(Tuple<String, Integer> mot:motsClefs) {
                    if(mot.x.contains(searchString) && !newMotsClefs.contains(mot)) {
                        newMotsClefs.add(mot);
                    }
                }

                ListAdapter newAdapt = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
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
    }

    private void callNextPage() {
        String params = "?";
        for(Tuple<String, Integer> mot:motsSelectionnes) {
            try {
                params += "keywords=" + URLEncoder.encode(mot.x, "UTF-8") + "&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params = params.substring(0,params.length()-1);
        }
        GetTask getCircuitsByKeywordsThread = new GetTask(Config.getRequest(Config.GET_CIRCUITS_KEYWORD + params));
        getCircuitsByKeywordsThread.delegate = new HandleGetCircuitsByKeywordsResponse(this);
        getCircuitsByKeywordsThread.execute();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tours) {
            Intent intent = new Intent(VisiterLyonActivity.this, Parcours.class);
            startActivity(intent);
        } else if (id == R.id.nav_friends) {
            Toast.makeText(VisiterLyonActivity.this, "Pas encore implémenté", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.create_parcours) {
            Intent intent = new Intent(VisiterLyonActivity.this, CreerParcours.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Toast.makeText(VisiterLyonActivity.this, "Pas encore implémenté", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_preferences) {
            Toast.makeText(VisiterLyonActivity.this, "Pas encore implémenté", Toast.LENGTH_SHORT).show();
        } else if(id == R.id.nav_login){
            Intent intent = new Intent(VisiterLyonActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_logout){

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().getItem(0).setChecked(true);
            // logout
            PostTask logout_thread = new PostTask(Config.getRequest(Config.LOGOUT));
            JsonObject emptyJson = new JsonObject();
            logout_thread.delegate = new HandleLogout(this);
            logout_thread.execute(emptyJson);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void processFinish(JsonObject results) {
        if (results == null || !results.get("status").getAsString().equals("OK")) {
            Toast.makeText(VisiterLyonActivity.this, "Veuillez vous connecter avant d'utiliser nos services.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(VisiterLyonActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            GetTask getKeywordsThread = new GetTask(Config.getRequest(Config.GET_KEYWORDS_OF_CIRCUIT));
            getKeywordsThread.delegate = new HandleGetKeywordsResponse(this);
            getKeywordsThread.execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkResumeData();
    }

    private void checkResumeData() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        // désactiver item home (on est dessus)
        navigationView.getMenu().getItem(0).setChecked(true);

        // on se log -> si ça marche pas on lance l'interface de login
        PostTask login_thread = new PostTask(Config.getRequest(Config.LOGIN));
        login_thread.delegate = this;
        String pseudo = User.getInstance().getUsername();
        String mdp = User.getInstance().getPassword();
        JsonObject user = new JsonObject();
        user.addProperty("password", mdp);
        user.addProperty("username", pseudo);
        login_thread.execute(user);

        TextView usernameText = (TextView) header.findViewById(R.id.username_textview_header);
        usernameText.setText(User.getInstance().getUsername());
        TextView emailText = (TextView) header.findViewById(R.id.email_textview_header);
        emailText.setText(User.getInstance().getEmail());
    }

    public void keywordsReceived(JsonObject results){
        if(results !=null){
            if (results.get("status").getAsString().equals("OK")) {
                JsonArray keywords = (JsonArray)results.get("keywords");
                for(JsonElement k: keywords){
                    String kName = ((JsonObject)k).get("name").getAsString();
                    int id = ((JsonObject)k).get("id").getAsInt();
                    Tuple<String, Integer> keywordToAdd = new Tuple<>(kName, id);
                    if(!motsClefs.contains(keywordToAdd))
                        motsClefs.add(keywordToAdd);
                    ListView listMotsProposes = (ListView) findViewById(R.id.motsClefs_listView);
                    ListAdapter newAdapt = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                    listMotsProposes.setAdapter(newAdapt);
                }
            }else{
                if(results.get("error").getAsString().contains("Please login")){
                    Toast.makeText(VisiterLyonActivity.this, "Veuillez vous connecter avant d'utiliser nos services.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VisiterLyonActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(VisiterLyonActivity.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void logoutReceived(JsonObject results){
        if (results == null || !results.get("status").getAsString().equals("OK")) {
            System.err.println("ERROR LOGOUT : " + results);
        }
        finish();
    }

    public void circuitsReceived(JsonObject results){
        if(results !=null){
            if (results.get("status").getAsString().equals("OK")) {
                double min = Math.floor((double) rangeSeekBar.getSelectedMinValue()/10*100)/100;
                double max = Math.floor((double) rangeSeekBar.getSelectedMaxValue()/10*100)/100;
                JsonArray circuits = (JsonArray)results.get("circuits");
                ArrayList<Circuit> circuitsToPass = new ArrayList<>();
                for(JsonElement c: circuits){
                    Circuit circuitToAdd = new Circuit(c.getAsJsonObject());
                    if(circuitToAdd.getLengthKm() >= min && circuitToAdd.getLengthKm() <= max)
                        circuitsToPass.add(circuitToAdd);
                }
                if(circuitsToPass.size() == 0){
                    Toast.makeText(VisiterLyonActivity.this, "Désolé, aucun parcours ne correspond à vos critères.", Toast.LENGTH_SHORT).show();
                    return;
                }
                CurrentCircuits.getInstance().setListOfCircuits(circuitsToPass);
                Intent intent = new Intent(VisiterLyonActivity.this, ListCircuit.class);
                startActivity(intent);
            }else{
                if(results.get("error").getAsString().contains("Please login")){
                    Toast.makeText(VisiterLyonActivity.this, "Veuillez vous connecter avant d'utiliser nos services.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VisiterLyonActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(VisiterLyonActivity.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

class HandleGetKeywordsResponse implements GetTask.AsyncResponse{

    private VisiterLyonActivity visiterLyonActivity;

    public HandleGetKeywordsResponse(VisiterLyonActivity visiterLyonActivity) {
        this.visiterLyonActivity = visiterLyonActivity;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.visiterLyonActivity.keywordsReceived(results);
    }
}

class HandleGetCircuitsByKeywordsResponse implements GetTask.AsyncResponse{

    private VisiterLyonActivity visiterLyonActivity;

    public HandleGetCircuitsByKeywordsResponse(VisiterLyonActivity visiterLyonActivity) {
        this.visiterLyonActivity = visiterLyonActivity;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.visiterLyonActivity.circuitsReceived(results);
    }
}

class HandleLogout implements PostTask.AsyncResponse{

    private VisiterLyonActivity visiterLyonActivity;

    public HandleLogout(VisiterLyonActivity visiterLyonActivity) {
        this.visiterLyonActivity = visiterLyonActivity;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.visiterLyonActivity.logoutReceived(results);
    }
}

class Tuple<X, Y> {
    public final X x;
    public final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return x.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        final Tuple other = (Tuple) obj;
        return x.toString().equals(other.x.toString()) && y == other.y;
    }
}
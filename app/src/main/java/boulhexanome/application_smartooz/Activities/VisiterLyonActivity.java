package boulhexanome.application_smartooz.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.WebServices.GetTask;
import boulhexanome.application_smartooz.WebServices.PostTask;

public class VisiterLyonActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PostTask.AsyncResponse {

    private List<Tuple<String, Integer>> motsSelectionnes;
    private ActionBar toolbar;
    private List<Tuple<String, Integer>> motsClefs;

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

                motsSelectionnes.add(keyword);
                motsClefs.remove(keyword);

                if(edittext.getText().toString().length() > 0) {
                    String searchString = edittext.getText().toString();
                    List<Tuple<String, Integer>> newMotsClefs = new ArrayList<>();
                    for(Tuple<String, Integer> mot:motsClefs) {
                        if(mot.x.startsWith(searchString)) {
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

                System.out.println(motsSelectionnes);
            }
        });

        listMotsChoisis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tuple<String, Integer> keyword = (Tuple<String, Integer>) listMotsChoisis.getItemAtPosition(position);

                motsSelectionnes.remove(keyword);
                motsClefs.add(keyword);

                if(edittext.getText().toString().length() > 0) {
                    String searchString = edittext.getText().toString();
                    List<Tuple<String, Integer>> newMotsClefs = new ArrayList<>();
                    for (Tuple<String, Integer> mot : motsClefs) {
                        if (mot.x.startsWith(searchString)) {
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



                System.out.println(motsSelectionnes);
            }
        });

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = edittext.getText().toString();
                List<Tuple<String, Integer>> newMotsClefs = new ArrayList<>();

                if(searchString.length() > 0 && !motsClefs.contains(searchString) && !motsSelectionnes.contains(searchString)) {
                    newMotsClefs.add(new Tuple(searchString, -1));
                }

                for(Tuple<String, Integer> mot:motsClefs) {
                    if(mot.x.startsWith(searchString)) {
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

        SeekBar longueurSeekBar = (SeekBar) findViewById(R.id.longueur_seekBar);
        final TextView longueurValue = (TextView) findViewById(R.id.longueurVal_textView);
        longueurSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                longueurValue.setText(String.valueOf(progress) + " m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        checkResumeData();
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
        if (results != null && results.get("status").getAsString().equals("OK")) {
            JsonArray keywords = (JsonArray)results.get("keywords");
            for(JsonElement k: keywords){
                String kName = ((JsonObject)k).get("name").getAsString();
                int id = ((JsonObject)k).get("id").getAsInt();
                motsClefs.add(new Tuple<>(kName, id));
                ListView listMotsProposes = (ListView) findViewById(R.id.motsClefs_listView);
                ListAdapter newAdapt = new ArrayAdapter<>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                listMotsProposes.setAdapter(newAdapt);
            }
        }
    }

    public void logoutReceived(JsonObject results){
        if (results == null || !results.get("status").getAsString().equals("OK")) {
            System.err.println("ERROR LOGOUT : " + results);
        }else{
            finish();
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
}
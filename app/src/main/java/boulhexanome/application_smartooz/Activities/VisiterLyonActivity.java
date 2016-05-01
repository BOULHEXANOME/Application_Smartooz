package boulhexanome.application_smartooz.Activities;



import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.RangeSeekBar;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.WebServices.PostTask;

public class VisiterLyonActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PostTask.AsyncResponse {

    private List<String> motsSelectionnes;
    private ActionBar toolbar;
    private List<String> motsClefs;

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

        // Range seekbar
        final RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(this);
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

        motsSelectionnes = new ArrayList<String>();
        motsClefs = new ArrayList<String>();

        motsClefs.add("banane");
        motsClefs.add("babouin");
        motsClefs.add("pomme");
        motsClefs.add("clementine");
        motsClefs.add("orange");
        motsClefs.add("pomelo");
        motsClefs.add("fraise");

        final ListView listMotsProposes = (ListView) findViewById(R.id.motsClefs_listView);
        final ListView listMotsChoisis = (ListView) findViewById(R.id.motsChoisis_listView);
        final ListAdapter adapt = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsClefs);
        final ListAdapter adaptChoisi = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);

        listMotsProposes.setAdapter(adapt);
        listMotsChoisis.setAdapter(adaptChoisi);

        listMotsProposes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String texte = (String) listMotsProposes.getItemAtPosition(position);

                motsSelectionnes.add(texte);
                motsClefs.remove(texte);

                if(edittext.getText().toString().length() > 0) {
                    String searchString = edittext.getText().toString();
                    List<String> newMotsClefs = new ArrayList<String>();
                    for(String mot:motsClefs) {
                        if(mot.startsWith(searchString)) {
                            newMotsClefs.add(mot);
                        }
                    }

                    ListAdapter newAdapt = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
                    listMotsProposes.setAdapter(newAdapt);

                } else {
                    ListAdapter newAdaptProposes = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                    listMotsProposes.setAdapter(newAdaptProposes);
                }

                ListAdapter newAdaptChoisi = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);
                listMotsChoisis.setAdapter(newAdaptChoisi);

                System.out.println(motsSelectionnes);
            }
        });

        listMotsChoisis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String texte = (String) listMotsChoisis.getItemAtPosition(position);

                motsSelectionnes.remove(texte);
                motsClefs.add(texte);

                if(edittext.getText().toString().length() > 0) {
                    String searchString = edittext.getText().toString();
                    List<String> newMotsClefs = new ArrayList<String>();
                    for (String mot : motsClefs) {
                        if (mot.startsWith(searchString)) {
                            newMotsClefs.add(mot);
                        }
                    }

                    ListAdapter newAdapt = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
                    listMotsProposes.setAdapter(newAdapt);
                } else {
                    ListAdapter newAdaptProposes = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                    listMotsProposes.setAdapter(newAdaptProposes);
                }

                ListAdapter newAdaptChoisis = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);
                listMotsChoisis.setAdapter(newAdaptChoisis);



                System.out.println(motsSelectionnes);
            }
        });

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = edittext.getText().toString();
                List<String> newMotsClefs = new ArrayList<String>();

                for(String mot:motsClefs) {
                    if(mot.startsWith(searchString)) {
                        newMotsClefs.add(mot);
                    }
                }

                ListAdapter newAdapt = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
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
            logout_thread.delegate = this;
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
}

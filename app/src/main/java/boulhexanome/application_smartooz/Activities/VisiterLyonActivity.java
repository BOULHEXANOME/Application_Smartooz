package boulhexanome.application_smartooz.Activities;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.R;

public class VisiterLyonActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView usernameText = (TextView) header.findViewById(R.id.username_textview_header);
        usernameText.setText(User.getInstance().getUsername());
        TextView emailText = (TextView) header.findViewById(R.id.email_textview_header);
        emailText.setText(User.getInstance().getEmail());


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

        // on se log -> si ça marche pas on lance l'interface de login

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            Intent intent = new Intent(VisiterLyonActivity.this, VisiterLyonActivity.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_tours) {

            Intent intent = new Intent(VisiterLyonActivity.this, Parcours.class);
            startActivity(intent);



        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_preferences) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

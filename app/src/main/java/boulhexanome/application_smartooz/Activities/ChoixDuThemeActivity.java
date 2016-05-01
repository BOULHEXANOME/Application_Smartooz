package boulhexanome.application_smartooz.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.Model.User;
import boulhexanome.application_smartooz.Utils.Config;
import boulhexanome.application_smartooz.WebServices.PostTask;

public class ChoixDuThemeActivity extends AppCompatActivity implements PostTask.AsyncResponse{

    private List<String> motsClefs;
    private ActionBar toolbar;
    private List<String> motsSelectionnes;
    private Circuit circuit;

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

        final EditText edittext = (EditText)findViewById(R.id.motsClefsRech_editText);
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

                    ListAdapter newAdapt = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
                    listMotsProposes.setAdapter(newAdapt);

                } else {
                    ListAdapter newAdaptProposes = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                    listMotsProposes.setAdapter(newAdaptProposes);
                }

                ListAdapter newAdaptChoisi = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);
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

                    ListAdapter newAdapt = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
                    listMotsProposes.setAdapter(newAdapt);
                } else {
                    ListAdapter newAdaptProposes = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                    listMotsProposes.setAdapter(newAdaptProposes);
                }

                ListAdapter newAdaptChoisis = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsSelectionnes);
                listMotsChoisis.setAdapter(newAdaptChoisis);



                System.out.println(motsSelectionnes);
            }
        });

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = edittext.getText().toString();
                List<String> newMotsClefs = new ArrayList<String>();

                if(searchString.length() > 0 && !motsClefs.contains(searchString) && !motsSelectionnes.contains(searchString)) {
                    newMotsClefs.add(searchString);
                }

                for(String mot:motsClefs) {
                    if(mot.startsWith(searchString)) {
                        newMotsClefs.add(mot);
                    }
                }

                ListAdapter newAdapt = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, newMotsClefs);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
            places_array.add(circuit.getPlaces().get(i).getPosition().toString());
        }
        jsonObject.add("places", places_array);

        System.out.println(jsonObject);
        postTask.execute(jsonObject);
    }

    @Override
    public void processFinish(JsonObject results) {
        if (results != null) {
            System.out.println(results.toString());
            if (results.get("status").getAsString().equals("OK")) {
                Toast.makeText(ChoixDuThemeActivity.this, "Le parcours a bien été ajouté ! ", Toast.LENGTH_SHORT).show();
                setResult(2);
                User.getInstance().setCircuit_en_creation(new Circuit());
                finish();
            } else if (results.get("status").getAsString().equals("KO")) {
                Toast.makeText(ChoixDuThemeActivity.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChoixDuThemeActivity.this, "BUG", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

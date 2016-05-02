package boulhexanome.application_smartooz.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Activities.Adapters.ParcoursAdapter;
import boulhexanome.application_smartooz.Model.CurrentCircuits;
import boulhexanome.application_smartooz.R;

public class ListCircuit extends AppCompatActivity {
    private ListView listParcours;
    private ActionBar toolbar;
    private List<Circuit> parcours;
    private int filterByDeniveleAsc;
    private int filterByKmAsc;
    private int filterByNbVoteAsc;
    private int filterByNoteAsc;
    private Menu menu;
    private ParcoursAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_circuit);

        listParcours = (ListView) findViewById(R.id.listViewParcours);
        parcours = CurrentCircuits.getInstance().getListOfCircuits();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Parcours");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        filterByDeniveleAsc = 0;
        filterByKmAsc = 0;
        filterByNbVoteAsc = 0;
        filterByNoteAsc = 0;

        adapter = new ParcoursAdapter(ListCircuit.this, parcours);
        listParcours.setAdapter(adapter);
        listParcours.setClickable(true);
        listParcours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Circuit c = (Circuit)listParcours.getItemAtPosition(position);
                CurrentCircuits.getInstance().setSelectedCircuit(c);
                Intent myIntent = new Intent(ListCircuit.this, CircuitDetailsActivity.class);
                ListCircuit.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_list_circuit,menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        MenuItem deniv = menu.findItem(R.id.action_filter_denivele);
        MenuItem km = menu.findItem(R.id.action_filter_km);
        MenuItem pop = menu.findItem(R.id.action_filter_popularity);
        MenuItem note = menu.findItem(R.id.action_filter_notes);

        deniv.setTitle("Dénivelé");
        km.setTitle("Kilomètre");
        pop.setTitle("Popularité");
        note.setTitle("Notes");
        switch(item.getItemId()){
            case R.id.action_filter_denivele:
                if(filterByDeniveleAsc==-1){
                    filterByKmAsc = 0;
                    filterByNbVoteAsc = 0;
                    filterByNoteAsc = 0;
                    filterByDeniveleAsc = 1;
                    item.setTitle("Dénivelé ∧");
                    adapter.sort(new ComparateurDeniveleAsc());
                }else if(filterByDeniveleAsc==1){
                    filterByDeniveleAsc = 0;
                    item.setTitle("Dénivelé ∨");
                    adapter.sort(new ComparateurDeniveleDesc());
                }else{
                    filterByDeniveleAsc = 1;
                    item.setTitle("Dénivelé ∧");
                    adapter.sort(new ComparateurDeniveleAsc());
                }
                break;
            case R.id.action_filter_km:
                if(filterByKmAsc==-1){
                    filterByKmAsc = 1;
                    filterByNbVoteAsc = -1;
                    filterByNoteAsc = -1;
                    filterByDeniveleAsc = -1;
                    item.setTitle("Kilomètre ∧");
                    adapter.sort(new ComparateurKmAsc());
                }else if(filterByKmAsc==1){
                    filterByKmAsc = 0;
                    item.setTitle("Kilomètre ∨");
                    adapter.sort(new ComparateurKmDesc());
                }else{
                    filterByKmAsc = 1;
                    item.setTitle("Kilomètre ∧");
                    adapter.sort(new ComparateurKmAsc());
                }
                break;
            case R.id.action_filter_notes:
                if(filterByNoteAsc==-1){
                    filterByKmAsc = -1;
                    filterByNbVoteAsc = -1;
                    filterByNoteAsc = 1;
                    filterByDeniveleAsc = -1;
                    item.setTitle("Notes ∧");
                    adapter.sort(new ComparateurNoteAsc());
                }else if(filterByNoteAsc==1){
                    filterByNoteAsc = 0;
                    item.setTitle("Notes ∨");
                    adapter.sort(new ComparateurNoteDesc());
                }else{
                    filterByNoteAsc = 1;
                    item.setTitle("Notes ∧");
                    adapter.sort(new ComparateurNoteAsc());
                }
                break;
            case R.id.action_filter_popularity:
                if(filterByNbVoteAsc==-1){
                    filterByKmAsc = -1;
                    filterByNbVoteAsc = 1;
                    filterByNoteAsc = -1;
                    filterByDeniveleAsc = -1;
                    item.setTitle("Popularité ∧");
                    adapter.sort(new ComparateurPopulariteAsc());
                }else if(filterByNbVoteAsc==1){
                    filterByNbVoteAsc = 0;
                    item.setTitle("Popularité ∨");
                    adapter.sort(new ComparateurPopulariteDesc());
                }else{
                    filterByNbVoteAsc = 1;
                    item.setTitle("Popularité ∧");
                    adapter.sort(new ComparateurPopulariteAsc());
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ComparateurPopulariteDesc implements Comparator<Circuit> {
        @Override
        public int compare(Circuit c1, Circuit c2){
            return c1.getNumberOfVotes()-c2.getNumberOfVotes();
        }
    }

    public class ComparateurKmDesc implements Comparator<Circuit> {
        @Override
        public int compare(Circuit c1, Circuit c2){
            return (int)(c1.getLengthKm()-c2.getLengthKm());
        }
    }

    public class ComparateurDeniveleDesc implements Comparator<Circuit> {
        @Override
        public int compare(Circuit c1, Circuit c2){
            return (int)(c1.getDeniveleM()-c2.getDeniveleM());
        }
    }

    public class ComparateurNoteDesc implements Comparator<Circuit> {
        @Override
        public int compare(Circuit c1, Circuit c2){
            return (int)(c1.getNoteOn5()-c2.getNoteOn5());
        }
    }
    public class ComparateurPopulariteAsc implements Comparator<Circuit> {
        @Override
        public int compare(Circuit c1, Circuit c2){
            return c2.getNumberOfVotes()-c1.getNumberOfVotes();
        }
    }

    public class ComparateurKmAsc implements Comparator<Circuit> {
        @Override
        public int compare(Circuit c1, Circuit c2){
            return (int)(c2.getLengthKm()-c1.getLengthKm());
        }
    }

    public class ComparateurDeniveleAsc implements Comparator<Circuit> {
        @Override
        public int compare(Circuit c1, Circuit c2){
            return (int)(c2.getDeniveleM()-c1.getDeniveleM());
        }
    }

    public class ComparateurNoteAsc implements Comparator<Circuit> {
        @Override
        public int compare(Circuit c1, Circuit c2){
            return (int)(c2.getNoteOn5()-c1.getNoteOn5());
        }
    }
}

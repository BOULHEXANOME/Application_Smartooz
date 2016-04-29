package boulhexanome.application_smartooz;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.Place;

public class ListCircuit extends AppCompatActivity {
    private ListView listParcours;
    private ActionBar toolbar;
    private List<Circuit> parcours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_circuit);
        listParcours = (ListView) findViewById(R.id.listViewParcours);

        parcours = new ArrayList<Circuit>();
        String[] tags = {"try","better"};
        Circuit circuit = new Circuit("Randonnée dans les champs","grave stylé cette randonnée",(float)3.2,(float)5.0,3,300,300,tags,null);
        Circuit circuit2 = new Circuit("note 1","grave stylé cette randonnée",(float)3.2,(float)1.0,3,300,300,tags,null);
        Circuit circuit3 = new Circuit("note 4","grave stylé cette randonnée",(float)3.2,(float)3.0,3,300,300,tags,null);
        Circuit circuit4 = new Circuit("note 3","grave stylé cette randonnée",(float)3.2,(float)4.0,3,300,300,tags,null);
        parcours.add(circuit);
        parcours.add(circuit2);
        parcours.add(circuit3);
        parcours.add(circuit4);
        filterByNoteAsc();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Parcours");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        ParcoursAdapter adapter = new ParcoursAdapter(ListCircuit.this, parcours);
        listParcours.setAdapter(adapter);
    }

    public void filterByPopularityDesc(){
        Collections.sort(parcours, new ComparateurPopulariteDesc());
    }
    public void filterByLengthDesc(){
        Collections.sort(parcours, new ComparateurKmDesc());
    }
    public void filterByDeniveleDesc(){
        Collections.sort(parcours, new ComparateurDeniveleDesc());
    }
    public void filterByNoteDesc(){
        Collections.sort(parcours, new ComparateurNoteDesc());
    }
    public void filterByPopularityAsc(){
        Collections.sort(parcours, new ComparateurPopulariteAsc());
    }
    public void filterByLengthAsc(){
        Collections.sort(parcours, new ComparateurKmAsc());
    }
    public void filterByDeniveleAsc(){
        Collections.sort(parcours, new ComparateurDeniveleAsc());
    }
    public void filterByNoteAsc(){
        Collections.sort(parcours, new ComparateurNoteAsc());
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

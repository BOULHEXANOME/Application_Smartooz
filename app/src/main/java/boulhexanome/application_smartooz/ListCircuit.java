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
import java.util.List;

public class ListCircuit extends AppCompatActivity {
    private ListView listParcours;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_circuit);
        listParcours = (ListView) findViewById(R.id.listViewParcours);

        List<ParcoursObject> parcours = new ArrayList<ParcoursObject>();
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));
        parcours.add(new ParcoursObject(Color.RED,"100m","10 km","Randonnée du chasseur","#trucDeMerde #Pd #INSA"));

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Parcours");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        ParcoursAdapter adapter = new ParcoursAdapter(ListCircuit.this, parcours);
        listParcours.setAdapter(adapter);
    }
}

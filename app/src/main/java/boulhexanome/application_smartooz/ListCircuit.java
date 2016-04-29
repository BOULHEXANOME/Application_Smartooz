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

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.Place;

public class ListCircuit extends AppCompatActivity {
    private ListView listParcours;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_circuit);
        listParcours = (ListView) findViewById(R.id.listViewParcours);

        List<Circuit> parcours = new ArrayList<Circuit>();
        String[] tags = {"try","better"};
        Circuit circuit = new Circuit("Randonnée dans les champs","grave stylé cette randonnée",(float)3.2,(float)5.0,3,tags,null);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);
        parcours.add(circuit);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Parcours");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        ParcoursAdapter adapter = new ParcoursAdapter(ListCircuit.this, parcours);
        listParcours.setAdapter(adapter);
    }
}

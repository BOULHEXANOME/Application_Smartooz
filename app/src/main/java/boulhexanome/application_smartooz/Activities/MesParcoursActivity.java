package boulhexanome.application_smartooz.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import boulhexanome.application_smartooz.Activities.Adapters.ParcoursAdapter;
import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.CurrentCircuitsSearch;
import boulhexanome.application_smartooz.R;

public class MesParcoursActivity extends AppCompatActivity {
    private ListView listParcours;
    private ListView listParcoursCrees;
    private ActionBar toolbar;
    private List<Circuit> parcoursEffectues;
    private List<Circuit> parcoursCrees;
    private int filterByDeniveleAsc;
    private int filterByKmAsc;
    private int filterByNbVoteAsc;
    private int filterByNoteAsc;
    private Menu menu;
    private ParcoursAdapter adapterEffectues;
    private ParcoursAdapter adapterCrees;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_parcours);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Mes Parcours");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        // ---------------- PARCOURS CREES

        listParcoursCrees = (ListView) findViewById(R.id.mesParc_listView2);

        //parcoursCrees = CurrentCircuits.getInstance().getListOfCircuits();
        // Pour les tests :
        parcoursCrees = new ArrayList<Circuit>();
        ArrayList<String> keywordsCrees = new ArrayList<>();
        keywordsCrees.add("tag1");
        keywordsCrees.add("tag2");
        keywordsCrees.add("tag3");
        Circuit circTestCrees = new Circuit("Parcours 1", "Description", (float) 10.0, (float) 3.0, 300, 0, 10, keywordsCrees, null, 1);
        Circuit circTestCrees2 = new Circuit("Parcours 1", "Description", (float) 10.0, (float) 4.0, 300, 0, 10, keywordsCrees, null, 1);
        Circuit circTestCrees3 = new Circuit("Parcours 1", "Description", (float) 10.0, (float) 2.0, 300, 0, 10, keywordsCrees, null, 1);
        parcoursCrees.add(circTestCrees);
        parcoursCrees.add(circTestCrees2);
        parcoursCrees.add(circTestCrees3);
        parcoursCrees.add(circTestCrees);

        if(!parcoursCrees.isEmpty()) {
            TextView parcCrees_text = (TextView) findViewById(R.id.parcCrees_textView);
            assert parcCrees_text != null;
            parcCrees_text.setVisibility(View.VISIBLE);

            View barre = findViewById(R.id.barreSeparation);
            assert  barre != null;
            barre.setVisibility(View.VISIBLE);

            listParcoursCrees.setMinimumHeight(200);

            filterByDeniveleAsc = 0;
            filterByKmAsc = 0;
            filterByNbVoteAsc = 0;
            filterByNoteAsc = 0;

            adapterCrees = new ParcoursAdapter(MesParcoursActivity.this, parcoursCrees);
            listParcoursCrees.setAdapter(adapterCrees);
            listParcoursCrees.setClickable(true);

            /*listParcoursCrees.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });*/
            setListViewHeightBasedOnChildren(listParcoursCrees);

            listParcoursCrees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Circuit c = (Circuit)listParcoursCrees.getItemAtPosition(position);
                    CurrentCircuitsSearch.getInstance().setSelectedCircuit(c);
                    Intent myIntent = new Intent(MesParcoursActivity.this, CircuitDetailsActivity.class);
                    MesParcoursActivity.this.startActivity(myIntent);
                }
            });
        }

        // --------- PARCOURS EFFECTUES

        listParcours = (ListView) findViewById(R.id.mesParc_listView1);

        //parcoursEffectues = CurrentCircuits.getInstance().getListOfCircuits();
        // Pour les tests :
        parcoursEffectues = new ArrayList<>();
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("tag1");
        keywords.add("tag2");
        keywords.add("tag3");
        Circuit circTest = new Circuit("Parcours 1", "Description", (float) 10.0, (float) 3.0, 300, 0, 10, keywords, null, 1);
        Circuit circTest2 = new Circuit("Parcours 1", "Description", (float) 10.0, (float) 4.0, 300, 0, 10, keywords, null, 1);
        Circuit circTest3 = new Circuit("Parcours 1", "Description", (float) 10.0, (float) 2.0, 300, 0, 10, keywords, null, 1);
        parcoursEffectues.add(circTest);
        parcoursEffectues.add(circTest2);
        parcoursEffectues.add(circTest3);
        parcoursEffectues.add(circTest);

        TextView parcEffectues_text = (TextView) findViewById(R.id.parcEffectues_textView);
        assert parcEffectues_text != null;
        parcEffectues_text.setVisibility(View.VISIBLE);

        filterByDeniveleAsc = 0;
        filterByKmAsc = 0;
        filterByNbVoteAsc = 0;
        filterByNoteAsc = 0;

        adapterEffectues = new ParcoursAdapter(MesParcoursActivity.this, parcoursEffectues);
        listParcours.setAdapter(adapterEffectues);
        listParcours.setClickable(true);

        /*listParcours.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
        setListViewHeightBasedOnChildren(listParcours);

        listParcours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Circuit c = (Circuit)listParcours.getItemAtPosition(position);
                CurrentCircuitsSearch.getInstance().setSelectedCircuit(c);
                Intent myIntent = new Intent(MesParcoursActivity.this, CircuitDetailsActivity.class);
                MesParcoursActivity.this.startActivity(myIntent);
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
                    adapterEffectues.sort(new ComparateurDeniveleAsc());
                    adapterCrees.sort(new ComparateurDeniveleAsc());
                }else if(filterByDeniveleAsc==1){
                    filterByDeniveleAsc = 0;
                    item.setTitle("Dénivelé ∨");
                    adapterEffectues.sort(new ComparateurDeniveleDesc());
                    adapterCrees.sort(new ComparateurDeniveleDesc());
                }else{
                    filterByDeniveleAsc = 1;
                    item.setTitle("Dénivelé ∧");
                    adapterEffectues.sort(new ComparateurDeniveleAsc());
                    adapterCrees.sort(new ComparateurDeniveleAsc());
                }
                break;
            case R.id.action_filter_km:
                if(filterByKmAsc==-1){
                    filterByKmAsc = 1;
                    filterByNbVoteAsc = -1;
                    filterByNoteAsc = -1;
                    filterByDeniveleAsc = -1;
                    item.setTitle("Kilomètre ∧");
                    adapterEffectues.sort(new ComparateurKmAsc());
                    adapterCrees.sort(new ComparateurKmAsc());
                }else if(filterByKmAsc==1){
                    filterByKmAsc = 0;
                    item.setTitle("Kilomètre ∨");
                    adapterEffectues.sort(new ComparateurKmDesc());
                    adapterCrees.sort(new ComparateurKmDesc());
                }else{
                    filterByKmAsc = 1;
                    item.setTitle("Kilomètre ∧");
                    adapterEffectues.sort(new ComparateurKmAsc());
                    adapterCrees.sort(new ComparateurKmAsc());
                }
                break;
            case R.id.action_filter_notes:
                if(filterByNoteAsc==-1){
                    filterByKmAsc = -1;
                    filterByNbVoteAsc = -1;
                    filterByNoteAsc = 1;
                    filterByDeniveleAsc = -1;
                    item.setTitle("Notes ∧");
                    adapterEffectues.sort(new ComparateurNoteAsc());
                    adapterCrees.sort(new ComparateurNoteAsc());
                }else if(filterByNoteAsc==1){
                    filterByNoteAsc = 0;
                    item.setTitle("Notes ∨");
                    adapterEffectues.sort(new ComparateurNoteDesc());
                    adapterCrees.sort(new ComparateurNoteDesc());
                }else{
                    filterByNoteAsc = 1;
                    item.setTitle("Notes ∧");
                    adapterEffectues.sort(new ComparateurNoteAsc());
                    adapterCrees.sort(new ComparateurNoteAsc());
                }
                break;
            case R.id.action_filter_popularity:
                if(filterByNbVoteAsc==-1){
                    filterByKmAsc = -1;
                    filterByNbVoteAsc = 1;
                    filterByNoteAsc = -1;
                    filterByDeniveleAsc = -1;
                    item.setTitle("Popularité ∧");
                    adapterEffectues.sort(new ComparateurPopulariteAsc());
                    adapterCrees.sort(new ComparateurPopulariteAsc());
                }else if(filterByNbVoteAsc==1){
                    filterByNbVoteAsc = 0;
                    item.setTitle("Popularité ∨");
                    adapterEffectues.sort(new ComparateurPopulariteDesc());
                    adapterCrees.sort(new ComparateurPopulariteDesc());
                }else{
                    filterByNbVoteAsc = 1;
                    item.setTitle("Popularité ∧");
                    adapterEffectues.sort(new ComparateurPopulariteAsc());
                    adapterCrees.sort(new ComparateurPopulariteAsc());
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

package boulhexanome.application_smartooz;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class VisiterLyonActivity extends AppCompatActivity {

    private List<String> motsSelectionnes;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiter_lyon);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Visiter Lyon");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        final EditText edittext = (EditText)findViewById(R.id.rech_editText);
        assert edittext != null;

        motsSelectionnes = new ArrayList<String>();

        final List<String> motsClefs = new ArrayList<String>();
        motsClefs.add("banane");
        motsClefs.add("babouin");
        motsClefs.add("pomme");
        motsClefs.add("clementine");

        final ListView list = (ListView) findViewById(R.id.motsClefs_listView);
        final ListAdapter adapt = new ArrayAdapter<String>(VisiterLyonActivity.this, android.R.layout.simple_list_item_1, motsClefs);
        list.setAdapter(adapt);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String texte = (String) list.getItemAtPosition(position);
                if(!motsSelectionnes.contains(texte))
                {
                    motsSelectionnes.add(texte);
                    view.setBackgroundColor(Color.GRAY);
                } else {
                    motsSelectionnes.remove(texte);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }

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
                list.setAdapter(newAdapt);

                // marche pas

                for (int i = 0; i < list.getAdapter().getCount(); i++) {
                    if (motsSelectionnes.contains((String) list.getAdapter().getItem(i))) {
                        System.out.println("Position : " +i);
                        View vue = getViewByPosition(i, list);
                        System.out.println("Vue : "+vue);
                        System.out.println(vue.getContentDescription());
                        vue.setBackgroundColor(Color.GRAY);
                    }
                }
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

        SeekBar deniveleSeekBar = (SeekBar) findViewById(R.id.denivele_seekBar);
        final TextView deniveleValue = (TextView) findViewById(R.id.denivVal_textView);
        deniveleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                deniveleValue.setText(String.valueOf(progress) + " m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        System.out.println("Premiere position : "+firstListItemPosition+ " et dernière position : "+lastListItemPosition);

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}

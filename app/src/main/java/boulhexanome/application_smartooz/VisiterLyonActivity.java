package boulhexanome.application_smartooz;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class VisiterLyonActivity extends AppCompatActivity {

    private List<String> motsSelectionnes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiterLyon);
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
                /*for(int a = 0; a < parent.getChildCount(); a++)
                {
                    parent.getChildAt(a).setBackgroundColor(Color.TRANSPARENT);
                }*/
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

                //view.setBackgroundColor(Color.GRAY);
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
                /*
                for (int i = 0; i < list.getAdapter().getCount(); i++) {
                    for(String mot:newMotsClefs) {
                        if (mot.equals((String) list.getAdapter().getItem(i))) {
                            View vue = list.getChildAt(i);
                            System.out.println(vue.getContentDescription());
                            vue.setBackgroundColor(Color.GRAY);
                        }
                    }
                }*/
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

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}

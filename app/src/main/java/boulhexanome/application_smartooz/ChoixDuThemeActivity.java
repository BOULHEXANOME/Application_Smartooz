package boulhexanome.application_smartooz;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChoixDuThemeActivity extends AppCompatActivity {

    private List<String> motsClefs;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_du_theme);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Visiter Lyon");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);


        Button rechParcours = (Button) findViewById(R.id.rechercheParcours_button);

        assert rechParcours != null;
        rechParcours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMotsClefs();
                System.out.println(motsClefs);

                /*
                ListView list = (ListView) findViewById(R.id.listViewTest);
                ListAdapter adapt = new ArrayAdapter<String>(ChoixDuThemeActivity.this, android.R.layout.simple_list_item_1, motsClefs);
                list.setAdapter(adapt);

                Intent myIntent = new Intent(ChoixDuThemeActivity.this, Parcours.class);
                ChoixDuThemeActivity.this.startActivity(myIntent);
                */
            }
        });
    }

    public void getMotsClefs() {
        List<String> mClefs = null;

        EditText motsClefsEditText = (EditText) findViewById(R.id.motsclefs_editText);
        String motsClefsText = motsClefsEditText.getText().toString();

        mClefs = Arrays.asList(motsClefsText.split("\\s*;\\s*"));
        motsClefs = mClefs;
    }
}

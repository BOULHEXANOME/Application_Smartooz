package boulhexanome.application_smartooz;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

                Intent myIntent = new Intent(ChoixDuThemeActivity.this, MainActivity.class);
                ChoixDuThemeActivity.this.startActivity(myIntent);
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
}

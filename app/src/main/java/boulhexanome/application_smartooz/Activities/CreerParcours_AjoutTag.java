package boulhexanome.application_smartooz.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import boulhexanome.application_smartooz.R;

public class CreerParcours_AjoutTag extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_parcours__ajout_tag);

        Button boutonOk = (Button) findViewById(R.id.bouton_ok);
        boutonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreerParcours_AjoutTag.this, VisiterLyonActivity.class);
                startActivity(intent);
            }
        });
    }
}

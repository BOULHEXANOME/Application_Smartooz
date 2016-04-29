package boulhexanome.application_smartooz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.google.android.gms.maps.MapFragment;

public class CircuitDetailsActivity extends AppCompatActivity {

    private MapFragment mMapFragment;
    private ImageButton hideButton;
    boolean mapIsHidden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mapIsHidden = false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit_details);


        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.circuit_details_map);
        //mMapFragment.getMapAsync(this);

        hideButton = (ImageButton) findViewById(R.id.drag_button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        hideButton.setOnClickListener(new View.OnClickListener() {
        public void onClick(View arg0) {


            if(mapIsHidden)
            {
                mMapFragment.getView().setVisibility(View.GONE);
                hideButton.setImageResource(R.drawable.ic_slide_arrow_down);
                mapIsHidden = false;
            }
            else
            {
                mMapFragment.getView().setVisibility(View.VISIBLE);
                hideButton.setImageResource(R.drawable.ic_slide_arrow_up);
                mapIsHidden = true;
            }


        }
    });


    }

}


package boulhexanome.application_smartooz;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.gms.maps.MapFragment;

public class CircuitDetailsActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private MapFragment mMapFragment;
    private ScrollView mScrollView;
    private Button hideButton;
    private LinearLayout scrollButtonLayout;
    boolean mapIsHidden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuit_details);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = getSupportActionBar();
        toolbar.setTitle("Détails du circuit");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        mapIsHidden = false;

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.circuit_details_map);
        //mMapFragment.getMapAsync(this);

        //hideButton = (Button) findViewById(R.id.drag_button);
        //hideButton.setTransformationMethod(null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mScrollView = (ScrollView) findViewById(R.id.circuit_details_scrollview);

        scrollButtonLayout = (LinearLayout) findViewById(R.id.drag_button_layout);


        //hideButton.setOnClickListener(new View.OnClickListener() {
        scrollButtonLayout.setOnClickListener(new View.OnClickListener() {

        public void onClick(View arg0) {


            if(mapIsHidden)
            {
                mMapFragment.getView().setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
                //hideButton.setImageResource(R.drawable.ic_slide_arrow_down);
                mapIsHidden = false;
            }
            else
            {
                mMapFragment.getView().setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.GONE);
                //hideButton.setImageResource(R.drawable.ic_slide_arrow_up);
                mapIsHidden = true;
            }


        }
    });


    }

}


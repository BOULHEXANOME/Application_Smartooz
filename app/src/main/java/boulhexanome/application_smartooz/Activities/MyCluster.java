package boulhexanome.application_smartooz.Activities;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

public class MyCluster implements ClusterItem {
    private final LatLng mPosition;
    private int id;

    public MyCluster(double lat, double lng, int id){
        mPosition = new LatLng(lat, lng);
        this.id = id;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public int getId() {
        return id;
    }
}

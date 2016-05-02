package boulhexanome.application_smartooz.Activities;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyCluster implements ClusterItem {
    private final LatLng mPosition;

    public MyCluster(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}

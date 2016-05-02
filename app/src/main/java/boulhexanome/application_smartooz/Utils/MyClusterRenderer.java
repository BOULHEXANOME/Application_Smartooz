package boulhexanome.application_smartooz.Utils;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import boulhexanome.application_smartooz.Activities.CreerParcours;
import boulhexanome.application_smartooz.Activities.MyCluster;

/**
 * Created by Aiebobo on 02/05/2016.
 */
public class MyClusterRenderer extends DefaultClusterRenderer{

    private CreerParcours creerParcours;
    private GoogleMap mMap;
    private ClusterManager clusterManager;
    ArrayList<Marker> markers;

    public MyClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
    }

    public MyClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager, CreerParcours creerParcours) {
        super(context, map, clusterManager);
        this.creerParcours = creerParcours;
    }


    @Override
    protected void onClusterItemRendered(ClusterItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        if (creerParcours.getMarkers() != null) {
            for (Marker m : creerParcours.getMarkers()) {
                System.out.println("Liste Markers : " + creerParcours.getMarkers());
                if (marker != null) {
                    if (marker.getTitle().equals(m.getTitle())) {
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    }
                }
            }
        }
    }
}

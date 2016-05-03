package boulhexanome.application_smartooz.Utils;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
public class MyClusterRenderer extends DefaultClusterRenderer<MyCluster>{

    ArrayList<MyCluster> clusterAdded;

    public MyClusterRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
        clusterAdded = CreerParcours.clusterAdded;
    }

    @Override
    protected void onBeforeClusterItemRendered(MyCluster item, MarkerOptions markerOptions) {
        clusterAdded = CreerParcours.clusterAdded;
        if (clusterAdded.size() != 0) {
            System.out.println("Salut");
            for (int i = 0; i < CreerParcours.clusterAdded.size(); i++) {
                if (item.getId()== clusterAdded.get(i).getId()) {
                    System.out.println("Kikoo");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
            }
        }
        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}

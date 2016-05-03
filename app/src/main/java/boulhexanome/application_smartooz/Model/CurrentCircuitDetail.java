package boulhexanome.application_smartooz.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class CurrentCircuitDetail {

    private static CurrentCircuitDetail mInstance = null;

    public static CurrentCircuitDetail getInstance(){
        if(mInstance == null)
        {
            mInstance = new CurrentCircuitDetail();
        }
        return mInstance;
    }

    private Circuit circuitEnCours;
    private int placeIndex;
    public boolean toDelete = false;

    public Circuit getCircuitEnCours() {
        return circuitEnCours;
    }

    public void setCircuitEnCours(Circuit circuitEnCours) {
        this.circuitEnCours = circuitEnCours;
    }

    private CurrentCircuitDetail(){
        placeIndex = -1;
    }

    public int getPlaceIndex() {
        return placeIndex;
    }

    public void setPlaceIndex(int placeIndex) {
        this.placeIndex = placeIndex;
    }

    @Override
    protected void finalize() throws Throwable {
        getInstance().setCircuitEnCours(this.circuitEnCours);
        super.finalize(); // questionable, but you should ensure calling it somewhere.
    }
}

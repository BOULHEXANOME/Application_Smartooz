package boulhexanome.application_smartooz.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class CurrentCircuitDetail {

    private static CurrentCircuitDetail mInstance = null;
    private ArrayList<Integer> onEstPassePar = new ArrayList<>();

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

    public Place getClosePlace(double latitude, double longitude) {
        double rayonEnKM = 0.065;
        double facteurLatitude = 0.009043;
        double facteurLongitude = 0.0131043;

        Place placeLaPlusProche = null;
        double distanceLaPlusProche = 99999;
        if(circuitEnCours.getPlaces() == null){
            System.err.println("Error : places null");
            return null;
        }
        for(Place p: circuitEnCours.getPlaces()){
            LatLng position = p.getPosition();
            double deltaLatitude = position.latitude - latitude;
            double deltaLongitude = position.longitude - longitude;
            double deltaLatitudeKM = deltaLatitude / facteurLatitude;
            double deltaLongitudeKM = deltaLongitude / facteurLongitude;
            double distance = Math.sqrt(deltaLatitudeKM*deltaLatitudeKM + deltaLongitudeKM*deltaLongitudeKM);
            System.out.println(distance);
            if(distance < rayonEnKM && distance < distanceLaPlusProche){
                placeLaPlusProche = p;
                distanceLaPlusProche = distance;
            }
        }

        int indexTrouve = getCircuitEnCours().getPlaces().indexOf(placeLaPlusProche);
        if(indexTrouve != placeIndex) {
            onEstPassePar.add(indexTrouve);
            placeIndex = indexTrouve;
            return placeLaPlusProche;
        }
        return null;
    }

    public ArrayList<Integer> getOnEstPassePar() {
        return onEstPassePar;
    }

    public void setOnEstPassePar(ArrayList<Integer> onEstPassePar) {
        this.onEstPassePar = onEstPassePar;
    }
}

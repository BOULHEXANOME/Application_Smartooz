package boulhexanome.application_smartooz.Model;

import com.google.android.gms.maps.model.LatLng;

public class CurrentCircuitTravel {

    private static CurrentCircuitTravel mInstance = null;

    public static CurrentCircuitTravel getInstance(){
        if(mInstance == null)
        {
            mInstance = new CurrentCircuitTravel();
        }
        return mInstance;
    }

    private Circuit circuitEnCours;
    private int placeIndex;

    public Circuit getCircuitEnCours() {
        return circuitEnCours;
    }

    public void setCircuitEnCours(Circuit circuitEnCours) {
        this.circuitEnCours = circuitEnCours;
    }

    public CurrentCircuitTravel(){
        placeIndex = 0;
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
            double deltaLatitudeKM = deltaLatitude * facteurLatitude;
            double deltaLongitudeKM = deltaLongitude * facteurLongitude;
            double distance = deltaLatitudeKM*deltaLatitudeKM + deltaLongitudeKM*deltaLongitudeKM;
            System.out.println(distance);
            if(distance < rayonEnKM && distance < distanceLaPlusProche){
                placeLaPlusProche = p;
                distanceLaPlusProche = distance;
            }
        }

        return placeLaPlusProche;
    }
}

package boulhexanome.application_smartooz.Model;

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
}

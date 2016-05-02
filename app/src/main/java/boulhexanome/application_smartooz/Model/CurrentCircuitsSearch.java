package boulhexanome.application_smartooz.Model;

import java.util.ArrayList;

/**
 * Created by nicol_000 on 01/05/2016.
 */
public class CurrentCircuitsSearch {

    private static CurrentCircuitsSearch mInstance = null;

    public static CurrentCircuitsSearch getInstance(){
        if(mInstance == null)
        {
            mInstance = new CurrentCircuitsSearch();
        }
        return mInstance;
    }


    private Circuit selectedCircuit;
    private ArrayList<Circuit> listOfCircuits = new ArrayList<>();

    public Circuit getSelectedCircuit() {
        return selectedCircuit;
    }

    public void setSelectedCircuit(Circuit selectedCircuit) {
        this.selectedCircuit = selectedCircuit;
    }

    public ArrayList<Circuit> getListOfCircuits() {
        return listOfCircuits;
    }

    public void setListOfCircuits(ArrayList<Circuit> listOfCircuits) {
        this.listOfCircuits = listOfCircuits;
    }


}

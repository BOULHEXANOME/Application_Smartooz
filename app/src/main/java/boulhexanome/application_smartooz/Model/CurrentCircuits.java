package boulhexanome.application_smartooz.Model;

import java.util.ArrayList;

/**
 * Created by nicol_000 on 01/05/2016.
 */
public class CurrentCircuits {

    private static CurrentCircuits mInstance = null;

    public static CurrentCircuits getInstance(){
        if(mInstance == null)
        {
            mInstance = new CurrentCircuits();
        }
        return mInstance;
    }


    private Circuit selectedCircuit;
    private ArrayList<Circuit> listOfCircuits = new ArrayList<Circuit>();

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

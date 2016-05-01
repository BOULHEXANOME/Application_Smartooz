package boulhexanome.application_smartooz.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import boulhexanome.application_smartooz.Model.Place;

/**
 * Created by nicol_000 on 27/04/2016.
 */
public class Circuit {

    private String name;
    private String description;
    private float lengthKm;
    private float noteOn5;
    private int idUser;
    transient private ArrayList<String> keywords;
    transient private ArrayList<Place> places;
    private int numberOfVotes;
    private int deniveleM;
    private int id;

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public int getDeniveleM() {
        return deniveleM;
    }

    public void setDeniveleM(int deniveleM) {
        this.deniveleM = deniveleM;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLengthKm() {
        return lengthKm;
    }

    public void setLengthKm(float lengthKm) {
        this.lengthKm = lengthKm;
    }

    public float getNoteOn5() {
        return noteOn5;
    }

    public void setNoteOn5(float noteOn5) {
        this.noteOn5 = noteOn5;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public ArrayList<String>  getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String>  keywords) {
        this.keywords = keywords;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Circuit{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lengthKm=" + lengthKm +
                ", noteOn5=" + noteOn5 +
                ", idUser=" + idUser +
                ", keywords=" + keywords.toString() +
                ", places=" + places +
                ", numberOfVotes=" + numberOfVotes +
                ", deniveleM=" + deniveleM +
                ", id=" + id +
                '}';
    }

    public Circuit(String name, String description, float lengthKm, float noteOn5, int deniveleM, int idUser, int numberOfVotes, ArrayList<String>  keywords, ArrayList<Place> places, int id) {
        this.name = name;
        this.description = description;
        this.lengthKm = lengthKm;
        this.noteOn5 = noteOn5;
        this.idUser = idUser;
        this.keywords = keywords;
        this.places = places;
        this.numberOfVotes = numberOfVotes;
        this.deniveleM = deniveleM;
        this.id = id;
    }

    public Circuit(String name, ArrayList<Place> places) {
        this.name = name;
        this.places = places;
    }


    public Circuit(JsonObject jsonObject) {
        JsonArray keywords = jsonObject.getAsJsonArray("keywords");
        ArrayList<String> pi_keywords = new ArrayList<>();
        for (int j = 0; j < keywords.size(); j++) {
            pi_keywords.add(keywords.get(j).getAsJsonObject().get("name").getAsString());
        }
        this.deniveleM = jsonObject.get("height_difference_m").getAsInt();
        this.lengthKm = jsonObject.get("length_km").getAsFloat();
        this.name = jsonObject.get("name").getAsString();
        this.description = jsonObject.get("description").getAsString();
        this.idUser = jsonObject.get("id_user").getAsInt();
        this.noteOn5 = jsonObject.get("note_5").getAsFloat();
        this.numberOfVotes = jsonObject.get("nb_vote").getAsInt();
        this.keywords = pi_keywords;
        this.id = jsonObject.get("id").getAsInt();
    }



    public Circuit() {
        this.places = new ArrayList<Place>();
    }

    public void addPlace(Place place){
        this.places.add(place);
    }

    public void addAllPlaces(Collection<Place> places){
        this.places.addAll(places);
    }
}

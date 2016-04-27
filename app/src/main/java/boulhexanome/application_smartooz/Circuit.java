package boulhexanome.application_smartooz;

import java.util.Arrays;

/**
 * Created by nicol_000 on 27/04/2016.
 */
public class Circuit {


    private String name;
    private String description;
    private float lengthKm;
    private float noteOn5;
    private int idUser;
    private String[] keywords;
    private Place[] places;

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

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public Place[] getPlaces() {
        return places;
    }

    public void setPlaces(Place[] places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Circuit{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lengthKm=" + lengthKm +
                ", noteOn5=" + noteOn5 +
                ", idUser=" + idUser +
                ", keywords=" + Arrays.toString(keywords) +
                ", places=" + Arrays.toString(places) +
                '}';
    }

    public Circuit(String name, String description, float lengthKm, float noteOn5, int idUser, String[] keywords, Place[] places) {
        this.name = name;
        this.description = description;
        this.lengthKm = lengthKm;
        this.noteOn5 = noteOn5;
        this.idUser = idUser;
        this.keywords = keywords;
        this.places = places;
    }


}

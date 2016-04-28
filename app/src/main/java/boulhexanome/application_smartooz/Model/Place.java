package boulhexanome.application_smartooz.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by nicol_000 on 27/04/2016.
 */
public class Place {

    private LatLng position;
    private String address;
    private String phone;
    private String website;
    private String openingHours;
    private String name;
    private String description;
    private int idUser;
    private float noteOn5;
    private int numberOfVotes;
    private String[] keywords;

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public float getNoteOn5() {
        return noteOn5;
    }

    public void setNoteOn5(float noteOn5) {
        this.noteOn5 = noteOn5;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "Place{" +
                "position=" + position +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", idUser=" + idUser +
                ", noteOn5=" + noteOn5 +
                ", numberOfVotes=" + numberOfVotes +
                ", keywords=" + Arrays.toString(keywords) +
                '}';
    }

    public Place(LatLng position, String name) {
        this.position = position;
        this.name = name;
    }

    public Place(LatLng position, String address, String phone, String website, String openingHours, String name, String description, int idUser, float noteOn5, int numberOfVotes, String[] keywords) {
        this.position = position;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.openingHours = openingHours;
        this.name = name;
        this.description = description;
        this.idUser = idUser;
        this.noteOn5 = noteOn5;
        this.numberOfVotes = numberOfVotes;
        this.keywords = keywords;
    }

    public MarkerOptions toMarkerOptions(){
        return new MarkerOptions()
                .position(position)
                .title(name)
                .snippet(description);
    }

    public JsonObject toJsonObject(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("latitude",position.latitude);
        jsonObject.addProperty("longitude",position.longitude);
        jsonObject.addProperty("name",name);
        return jsonObject;
    }

}

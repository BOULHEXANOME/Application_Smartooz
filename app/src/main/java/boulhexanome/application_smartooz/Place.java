package boulhexanome.application_smartooz;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;

/**
 * Created by nicol_000 on 27/04/2016.
 */
public class Place {

    private double latitude;
    private double longitude;;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
                "latitude=" + latitude +
                ", longitude=" + longitude +
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

    public Place(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public Place(double latitude, double longitude, String address, String phone, String website, String openingHours, String name, String description, int idUser, float noteOn5, int numberOfVotes, String[] keywords) {
        this.latitude = latitude;
        this.longitude = longitude;
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
                .position(new LatLng(latitude,longitude))
                .title(name);
    }

}
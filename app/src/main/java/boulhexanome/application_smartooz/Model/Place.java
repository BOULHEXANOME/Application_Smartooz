package boulhexanome.application_smartooz.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
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
    private String urlImage;
    private int idUser;
    private float noteOn5;
    private int numberOfVotes;
    private ArrayList<String> keywords;
    private int id;

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
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

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addKeyword(String keyword){
        keywords.add(keyword);
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
                ", keywords=" + keywords +
                "' id=" + id +
                "' url image=" + urlImage +
                '}';
    }

    public Place(LatLng position, String name) {
        this.position = position;
        this.name = name;
        keywords = new ArrayList<>();
    }

    public Place(LatLng position, String address, String phone, String website, String openingHours, String name, String description, String urlImage, int idUser, float noteOn5, int numberOfVotes, ArrayList<String> keywords, int id) {
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
        this.id = id;
        this.urlImage = urlImage;
    }

    public Place(JsonObject jsonObject) {
        JsonArray keywords = jsonObject.getAsJsonArray("keywords");
        ArrayList<String> pi_keywords = new ArrayList<>();
        for (int j = 0; j < keywords.size(); j++) {
            pi_keywords.add(keywords.get(j).getAsJsonObject().get("name").getAsString());
        }
        this.position = new LatLng(jsonObject.get("lat").getAsDouble(), jsonObject.get("long").getAsDouble());
        this.address = jsonObject.get("address").getAsString();
        this.phone = jsonObject.get("phone").toString();
        this.website = jsonObject.get("website").toString();
        this.openingHours = jsonObject.get("openning_hours").getAsString();
        this.name = jsonObject.get("name").getAsString();
        this.description = jsonObject.get("description").getAsString();
        this.idUser = jsonObject.get("id_user").getAsInt();
        this.noteOn5 = jsonObject.get("note_5").getAsFloat();
        this.numberOfVotes = jsonObject.get("nb_vote").getAsInt();
        this.keywords = pi_keywords;
        this.id = jsonObject.get("id").getAsInt();
        this.urlImage = jsonObject.get("image").getAsString();
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

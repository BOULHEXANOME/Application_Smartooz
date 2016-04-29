package boulhexanome.application_smartooz;

/**
 * Created by Hugo on 28/04/2016.
 */
public class ParcoursObject {

    private int color;
    private String denivele;
    private String km;
    private String titre;
    private String tags;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDenivele() {
        return denivele;
    }

    public void setDenivele(String denivele) {
        this.denivele = denivele;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public ParcoursObject(int color, String denivele, String km, String titre, String tags) {
        this.color = color;
        this.denivele = denivele;
        this.km = km;
        this.titre = titre;
        this.tags = tags;
    }
}

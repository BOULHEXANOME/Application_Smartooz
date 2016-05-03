package boulhexanome.application_smartooz.Model;

public class CurrentPlaceDetail {

    private static CurrentPlaceDetail mInstance = null;

    public static CurrentPlaceDetail getInstance(){
        if(mInstance == null)
        {
            mInstance = new CurrentPlaceDetail();
        }
        return mInstance;
    }

    private Place placeEnCours;

    private CurrentPlaceDetail(){
    }

    public static CurrentPlaceDetail getmInstance() {
        return mInstance;
    }

    public Place getPlaceEnCours() {
        return placeEnCours;
    }

    public void setPlaceEnCours(Place placeEnCours) {
        this.placeEnCours = placeEnCours;
    }

    @Override
    protected void finalize() throws Throwable {
        getInstance().setPlaceEnCours(this.placeEnCours);
        super.finalize(); // questionable, but you should ensure calling it somewhere.
    }
}

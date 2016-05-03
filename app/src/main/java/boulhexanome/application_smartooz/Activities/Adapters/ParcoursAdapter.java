package boulhexanome.application_smartooz.Activities.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.places.Places;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.Place;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.Utils.Config;

/**
 * Created by Hugo on 28/04/2016.
 */
public class ParcoursAdapter extends ArrayAdapter<Circuit> {
    private String urlImage;

    public ParcoursAdapter(Context context, List<Circuit> parcours) {
        super(context, 0, parcours);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_liste_parcours, parent, false);
        }

        ParcoursViewHolder viewHolder = (ParcoursViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ParcoursViewHolder();
            viewHolder.nom = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.kilometre = (TextView) convertView.findViewById(R.id.textKm);
            viewHolder.denivele = (TextView) convertView.findViewById(R.id.textDenivele);
            viewHolder.tags = (TextView) convertView.findViewById(R.id.textViewTags);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.note = (RatingBar) convertView.findViewById(R.id.noteParcours);
            convertView.setTag(viewHolder);
        }


        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Circuit parcours = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.nom.setText(parcours.getName());
        viewHolder.kilometre.setText("Longueur : "+ parcours.getLengthKm() +" km");
        viewHolder.denivele.setText("Dénivelé : "+ parcours.getDeniveleM() +" m");
        ArrayList<String> tags = parcours.getKeywords();
        String tag="";
        for(int i = 0;i<tags.size();i++){
            if(i!=0){
                tag+=" ";
            }
            if(i>6){
                break;
            }
            tag += tags.get(i);

        }
        viewHolder.tags.setText(tag);
        viewHolder.note.setRating(parcours.getNoteOn5());
        viewHolder.note.setFocusable(false);

        urlImage = Config.PROTOCOL + "://" + Config.IP_SERV + ":" + Config.PORT + "/circuits/" + Integer.toString(parcours.getId());
        new DownloadImageTask(viewHolder.image).execute(urlImage);
        return convertView;
    }
    private class ParcoursViewHolder{
        public TextView nom;
        public TextView kilometre;
        public TextView denivele;
        public TextView tags;
        public ImageView image;
        public RatingBar note;
    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    @Override
    protected Bitmap doInBackground(String... urls) {
        System.err.println("enter the get");
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            InputStream in = null;
            try {
                in = new java.net.URL("http://www.mediterranee-air-training.fr/wp-content/plugins/lightbox/images/No-image-found.jpg").openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return mIcon11;
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

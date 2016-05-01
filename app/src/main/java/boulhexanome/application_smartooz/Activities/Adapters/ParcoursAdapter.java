package boulhexanome.application_smartooz.Activities.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.R;

/**
 * Created by Hugo on 28/04/2016.
 */
public class ParcoursAdapter extends ArrayAdapter<Circuit> {
    public ParcoursAdapter(Context context, List<Circuit> parcours) {
        super(context, 0, parcours);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_liste_parcours,parent, false);
        }

        ParcoursViewHolder viewHolder = (ParcoursViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ParcoursViewHolder();
            viewHolder.nom = (TextView) convertView.findViewById(R.id.titre);
            viewHolder.kilometre = (TextView) convertView.findViewById(R.id.textKm);
            viewHolder.denivele = (TextView) convertView.findViewById(R.id.textDenivele);
            viewHolder.tags = (TextView) convertView.findViewById(R.id.textViewTags);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Circuit parcours = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.nom.setText(parcours.getName());
        viewHolder.kilometre.setText(String.valueOf(parcours.getLengthKm())+"km");
        viewHolder.denivele.setText(String.valueOf(parcours.getLengthKm()));
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
        //TODO
        viewHolder.image.setImageDrawable(new ColorDrawable(Color.RED));

        return convertView;
    }
    private class ParcoursViewHolder{
        public TextView nom;
        public TextView kilometre;
        public TextView denivele;
        public TextView tags;
        public ImageView image;
    }
}
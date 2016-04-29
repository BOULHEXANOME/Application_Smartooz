package boulhexanome.application_smartooz;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hugo on 28/04/2016.
 */
public class ParcoursAdapter extends ArrayAdapter<ParcoursObject> {
    public ParcoursAdapter(Context context, List<ParcoursObject> parcours) {
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
        ParcoursObject parcours = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.nom.setText(parcours.getTitre());
        viewHolder.kilometre.setText(parcours.getKm());
        viewHolder.denivele.setText(parcours.getDenivele());
        viewHolder.tags.setText(parcours.getTags());
        viewHolder.image.setImageDrawable(new ColorDrawable(parcours.getColor()));

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
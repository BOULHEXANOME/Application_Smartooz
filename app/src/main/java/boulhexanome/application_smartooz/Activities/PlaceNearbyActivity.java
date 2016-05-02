package boulhexanome.application_smartooz.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import boulhexanome.application_smartooz.Model.Circuit;
import boulhexanome.application_smartooz.Model.CurrentCircuitTravel;
import boulhexanome.application_smartooz.R;
import boulhexanome.application_smartooz.WebServices.GetTask;

public class PlaceNearbyActivity extends AppCompatActivity {
    private TextView textViewDescription;
    private TextView textViewTitle;
    String API_KEY = "AIzaSyDWlPi3Sbzq33C6yK-dem9XPga0E9a402U";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_nearby);
        textViewDescription = (TextView)findViewById(R.id.textViewDescriptionPlace);
        textViewTitle = (TextView)findViewById(R.id.textViewTitlePlace);
        textViewDescription.setVerticalScrollBarEnabled(true);
        Circuit c = CurrentCircuitTravel.getInstance().getCircuitEnCours();
        String nom = c.getName();
        nom+="+lyon";
        String keywords = "";
        try {
            keywords = URLEncoder.encode(nom, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Querying(keywords);
        } catch (IOException e) {
            e.printStackTrace();
            textViewDescription.append("nike sa m...");
        }
        /*Result response =  new Result();
        new RequestTask(response).execute("https://www.google.fr/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q="+keyWords);
        */
        //AIzaSyDWlPi3Sbzq33C6yK-dem9XPga0E9a402U
        //002950127685759816034:9e5qtixkpfm
    }
    /* public void TheQueryIsHere() {
        String sparqlQueryString1 = "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
                "PREFIX dbo: <http://dbpedia.org/ontology/>" +
                "   SELECT * WHERE {" +
                "   ?s geo:lat ?lat ." +
                "   ?s geo:long ?long ."+
                "   FILTER (?lat>= 45.75 && ?long>= 4.8 && ?lat<= 45.77 && ?long<= 4.9)"+
                "   }"+
                "LIMIT 100";

        Query query = QueryFactory.create(sparqlQueryString1);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

        ResultSet results = qexec.execSelect();
        results.
        ResultSetFormatter.out(System.out, results, query);

        qexec.close();
        TextView zoneText = (TextView) findViewById(R.id.textViewDescription);
   }*/
    public void Querying (String parameters) throws IOException {
        GetTask getPlaceNearby = new GetTask("https://kgsearch.googleapis.com/v1/entities:search?query="+parameters+"&key="+API_KEY+"&limit=1&indent=True&languages=fr");
        getPlaceNearby.delegate = new HandleGetPlaceNearbyResponse(this);
        JsonObject j = new JsonObject();
        getPlaceNearby.execute(j);
    }

    public void placeReceived(JsonObject results){
        JsonArray item = results.get("itemListElement").getAsJsonArray();
        JsonElement element = item.get(0);
        JsonObject object = element.getAsJsonObject().get("result").getAsJsonObject();
        JsonElement name = object.get("name");
        JsonElement descriptionCourte = object.get("description");
        JsonObject imagesURL = object.get("image").getAsJsonObject();
        JsonObject descriptionLongue = object.get("detailedDescription").getAsJsonObject();
        JsonElement description = descriptionLongue.get("articleBody");
        textViewTitle.setText("");
        textViewTitle.append(name.getAsString());
        textViewTitle.append("\r\n");
        textViewTitle.append(descriptionCourte.getAsString());
        textViewDescription.setText("");
        textViewDescription.append(description.getAsString());
        if(results !=null){
           //textViewDescription.append(results.toString());
        }
    }
}

class HandleGetPlaceNearbyResponse implements GetTask.AsyncResponse{

    private PlaceNearbyActivity placeNearbyActivity;

    public HandleGetPlaceNearbyResponse(PlaceNearbyActivity placeNearbyActivity) {
        this.placeNearbyActivity = placeNearbyActivity;
    }

    @Override
    public void processFinish(JsonObject results) {
        this.placeNearbyActivity.placeReceived(results);
    }
}


/*


class Result{
    String text;
    public void setText(String s){
        text = s;
    }
    public String getText(){
        return text;
    }
}
class RequestTask extends AsyncTask<String, String, String> {
    Result result;
    public RequestTask(Result r){
        result = r;
    }
    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result1) {
        super.onPostExecute(result1);
        result.setText(result1);
    }
}*/
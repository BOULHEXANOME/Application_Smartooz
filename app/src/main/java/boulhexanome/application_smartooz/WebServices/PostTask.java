package boulhexanome.application_smartooz.WebServices;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Nicolas on 28/04/2016.
 */
public class PostTask extends AsyncTask<JsonObject, Void, Void> {

    private String postURL;
    private JsonObject result = null;
    private int status = 0;
    public AsyncResponse delegate=null;

    public PostTask(String URL) {
        this.postURL = URL;
    }


    public interface AsyncResponse {
        void processFinish(JsonObject results);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        delegate.processFinish(result);
    }

    @Override
    protected Void doInBackground(JsonObject... params) {
        try {
            // Ouverture de la connexion
            URL url = new URL(postURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            if (params.length > 0) {
                final JsonObject realjObject = params[0];
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(realjObject.toString());
                out.close();
            }

            if (urlConnection.getInputStream() != null) {
                result = parseJson(urlConnection.getInputStream());
            }

            return null;

        } catch (ProtocolException e) {
            Log.e("ProtocolException", "Inscription.java : protocol exception");
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            Log.e("MalformedURLException", "Inscription.java : MalformedURLException");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e("IOException", "Inscription.java : IOException");
            e.printStackTrace();
            return null;
        }
    }

    protected JsonObject parseJson(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String inputLine;
        Gson gson = new Gson();
        try {
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
            String line = stringBuilder.toString();
            JsonParser parser = new JsonParser();
            JsonElement jsnelement = (JsonElement) parser.parse(line);
            JsonObject jsnobject = jsnelement.getAsJsonObject();
            in.close();
            return jsnobject;
        } catch (IOException e) {
            Log.e("IOException", "identifyUser : IOException");
            e.printStackTrace();
            return null;
        }
    }
}



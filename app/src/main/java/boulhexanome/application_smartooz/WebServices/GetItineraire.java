package boulhexanome.application_smartooz.WebServices;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import boulhexanome.application_smartooz.Model.Circuit;

import static boulhexanome.application_smartooz.Tools.generateGoogleMapURL;
import static boulhexanome.application_smartooz.Tools.parseJson;

/**
 * Created by Aiebobo on 26/04/2016.
 */
public class GetItineraire extends AsyncTask<URL, Void, Void> {

    private JsonObject result = null;
    private int status = 0;
    public AsyncResponse delegate=null;


    public interface AsyncResponse {
        void processFinish(JsonObject results);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        delegate.processFinish(result);
    }



    @Override
    protected Void doInBackground(URL... params) {
        try {

            final URL url = params[0];

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);

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
}



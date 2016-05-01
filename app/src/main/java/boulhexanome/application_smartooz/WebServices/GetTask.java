package boulhexanome.application_smartooz.WebServices;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import boulhexanome.application_smartooz.Model.User;

import static boulhexanome.application_smartooz.Utils.Tools.parseJson;

/**
 * Created by Nicolas on 28/04/2016.
 */
public class GetTask extends AsyncTask {

    private String postURL;
    private JsonObject result = null;
    private int status = 0;
    public AsyncResponse delegate=null;

    public GetTask(String URL) {
        this.postURL = URL;
    }


    public interface AsyncResponse {
        void processFinish(JsonObject results);
    }

    @Override
    protected void onPostExecute(Object o) {
        delegate.processFinish(result);
    }

    @Override
    protected Void doInBackground(Object objects[]) {
        try {
            // Ouverture de la connexion
            URL url = new URL(postURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);

            if(User.getInstance().getCookieManager().getCookieStore().getCookies().size() > 0)
            {
                urlConnection.setRequestProperty("Cookie",
                        TextUtils.join(";",  User.getInstance().getCookieManager().getCookieStore().getCookies()));
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
}



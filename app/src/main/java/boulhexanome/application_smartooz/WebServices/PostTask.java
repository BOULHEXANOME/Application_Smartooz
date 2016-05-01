package boulhexanome.application_smartooz.WebServices;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import boulhexanome.application_smartooz.Model.User;

import static boulhexanome.application_smartooz.Utils.Tools.parseJson;

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

            if(User.getInstance().getCookieManager().getCookieStore().getCookies().size() > 0)
            {
                urlConnection.setRequestProperty("Cookie",
                        TextUtils.join(";",  User.getInstance().getCookieManager().getCookieStore().getCookies()));
            }

            if (params.length > 0) {
                final JsonObject realjObject = params[0];
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(realjObject.toString());
                out.close();
            }

            if (urlConnection.getInputStream() != null) {
                result = parseJson(urlConnection.getInputStream());
            }

            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            List<String> cookiesHeader = headerFields.get("Set-Cookie");
            if(cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    User.getInstance().getCookieManager().getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }
            return null;

        } catch (ProtocolException e) {
            Log.e("ProtocolException", "PostTask.java : protocol exception");
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            Log.e("MalformedURLException", "PostTask.java : MalformedURLException");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e("IOException", "PostTask.java : IOException");
            e.printStackTrace();
            return null;
        }
    }
}



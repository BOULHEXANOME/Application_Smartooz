package boulhexanome.application_smartooz;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Aiebobo on 26/04/2016.
 */
public class WebService extends AsyncTask{

    private final String URL_long = "http://localhost:5000/register"; //on emulator, change localhost with 10.0.2.2

    Gson gson;

    public WebService() {
        gson = new Gson();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            getPoints();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream sendRequest(URL url) throws Exception {

        // Ouverture de la connexion
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.setRequestProperty("Content-Type","application/json");
        System.out.println("OKya");


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password","hugo");
        jsonObject.put("username","papin2");
        jsonObject.put("email","barbiche");

        System.out.println(jsonObject.toString());

        OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
        out.write(jsonObject.toString());
        out.close();

        return  urlConnection.getInputStream();
    }

    public void getPoints() throws Exception {
            InputStream inputStream = sendRequest(new URL(URL_long));
    }
}

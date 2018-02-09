package com.teamstartup.startup.drrobo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import android.widget.Toast;
/**
 * Created by Jinwei-PC on 1/10/2017.
 */

public class GetUrlContentTask extends AsyncTask<String, Integer, String> {

    public ProfileInteractionActivity delegate = null;
    public MedicationActivity delegate2 = null;
    public MedicationActivity delegate3 = null;
    public MainActivity delegate4 = null;

    protected String doInBackground(String... urls) {
        HttpURLConnection connection = null;
        String content = "", line;
        try {
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = rd.readLine()) != null) {
                content += line + "\n";
            }
            return content;
        } catch (MalformedURLException e){

        } catch (IOException e){

        } finally{
            connection.disconnect();
        }

        return content;
    }

    protected void onProgressUpdate(Integer... progress) {
    }


    protected void onPostExecute(String result) {
        // this is executed on the main thread after the process is over
        // update your UI he
        //displayMessage(result);
       // Toast.makeText(MainActivity.this, result,  Toast.LENGTH_SHORT).show();
        if(delegate != null){
            delegate.processFinish(result);
        } else if (delegate2 != null){
            delegate2.processFinish(result);
        } else if (delegate4 != null){
            delegate4.processFinish(result);
        }
        delegate = null;
        delegate2 = null;
        delegate4 = null;

    }
}
package com.expansion.lg.kimaru.expansion.sync;
import android.util.Log;

import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.MalformedURLException;


/**
 * Created by kimaru on 3/20/17.
 */

public class ApiClient {
    static String stream = null;

    public ApiClient(){
    }

    public String GetHTTPData(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // TODO: Get cookie
            // TODO: Open URL without calling method openConnection();
            //urlConnection.setRequestProperty("Cookie", "CookieVlaue");


            Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Log.d("Tremap", "CLIENT URL -- : "+urlString);
            Log.d("Tremap", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            // Check the connection status
            if(urlConnection.getResponseCode() == 200)
            {
                // if response code = 200 ok
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Read the BufferedInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                stream = sb.toString();
                // End reading...............

                // Disconnect the HttpURLConnection
                urlConnection.disconnect();
            }
            else
            {
                // Do something
                Log.d("Tremap", "==============================");
                Log.d("Tremap", "NOT 200 STATUS");
                Log.d("Tremap", "==============================");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.d("Tremap", "==============================");
            Log.d("Tremap", "MALFORMED -- : "+e.getMessage());
            Log.d("Tremap", "==============================");
        }catch(IOException e){
            e.printStackTrace();
            Log.d("Tremap", "==============================");
            Log.d("Tremap", "IO ERROR -- : "+ e.getMessage());
            Log.d("Tremap", "==============================");
        }finally {

        }
        // Return the data from specified url

        return stream;
    }
}

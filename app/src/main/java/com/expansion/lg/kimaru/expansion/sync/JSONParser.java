package com.expansion.lg.kimaru.expansion.sync;

import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;




/**
 * Created by kimaru on 3/20/17.
 */

public class JSONParser {
    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";

    private static final String TAG = HttpAuthHandler.class.getSimpleName();
//    private static final String TAG = HttpHandler.class.getSimpleName();

    // constructor
    public JSONParser(){}

    public String getJsonFromUrl(String apiUrl, String requestMethod){
        String response = null;
        //make HTTP Request
        try{
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);

            //Read the response
            InputStream dataFromUrl = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(dataFromUrl);
        } catch (MalformedURLException e){
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e){
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "IOException: " + e.getMessage());
        }catch (Exception e){
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

package com.expansion.lg.kimaru.expansion.other;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class UtilFunctions {

    public static String loadFromAsset(Context context, String filename) {
        String content = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return content;
    }

    public static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
        JSONObject mergedJSON = new JSONObject();
        try {

            Iterator i1 = json1.keys();
            Iterator i2 = json2.keys();
            String tempKey;
            while (i1.hasNext()) {
                tempKey = (String) i1.next();
                mergedJSON.put(tempKey, json1.get(tempKey));
            }

            while (i2.hasNext()) {
                tempKey = i2.next().toString();
                mergedJSON.put(tempKey, json2.get(tempKey));
            }

        } catch (JSONException e) {
            throw new RuntimeException("JSON Exception" + e);
        }
        return mergedJSON;
    }
}

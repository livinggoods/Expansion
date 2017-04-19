package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.expansion.lg.kimaru.expansion.mzigos.User;
import com.expansion.lg.kimaru.expansion.other.Constants;
import com.expansion.lg.kimaru.expansion.tables.UserTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kimaru on 3/21/17.
 */

public class UserDataSync {

    Context context;

    public UserDataSync(Context context){
        this.context = context;
    }

    public void pollNewUsers(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask getUsersTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = Constants.CLOUD_ADDRESS+"/api/v1/users/json";
                        new syncUsers().execute(url);
                    }
                });
            }
        };
        timer.schedule(getUsersTask, 0, 60*1000 * 30); //every 30 minutes
    }



    private class syncUsers extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            ApiClient hh = new ApiClient();
            stream = hh.GetHTTPData(urlString);
            return stream;
        }
        protected void onPostExecute(String stream){
            Toast.makeText(context, "Syncing users", Toast.LENGTH_SHORT).show();
            if(stream !=null){
                try{
                    JSONObject reader= new JSONObject(stream);
                    JSONArray recs = reader.getJSONArray("users");
                    List<User> userList = new ArrayList<User>();
                    for (int x = 0; x < recs.length(); x++){
                        User user = new User();
                        user.setId(recs.getJSONObject(x).getInt("id"));
                        user.setPassword(recs.getJSONObject(x).getString("app_name"));
                        user.setUsername(recs.getJSONObject(x).getString("username"));
                        user.setEmail(recs.getJSONObject(x).getString("email"));
                        user.setName(recs.getJSONObject(x).getString("name"));
                        user.setCountry(recs.getJSONObject(x).getString("country"));
                        UserTable userTable = new UserTable(context);
                        long id = userTable.addUser(user);

                    }
                    // process other data as this way..............

                }catch(JSONException e){
                    Toast.makeText(context, "ERROR :'( " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            } // if statement end
        } // onPostExecute() end
    }
}

package com.expansion.lg.kimaru.expansion.sync;

import android.content.Context;

import com.expansion.lg.kimaru.expansion.activity.SessionManagement;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TremapApiClient {


    private static Retrofit retrofit = null;


    public static Retrofit getClient(Context context) {

        if (retrofit==null) {

            SessionManagement session = new SessionManagement(context);

            String baseUrl = session.getCloudUrl();

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
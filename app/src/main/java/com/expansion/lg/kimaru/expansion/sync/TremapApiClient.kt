package com.expansion.lg.kimaru.expansion.sync

import android.content.Context

import com.expansion.lg.kimaru.expansion.activity.SessionManagement

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object TremapApiClient {


    private var retrofit: Retrofit? = null


    fun getClient(context: Context): Retrofit {

        if (retrofit == null) {

            val session = SessionManagement(context)

            val baseUrl = session.cloudUrl

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!
    }
}
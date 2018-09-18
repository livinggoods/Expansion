package com.expansion.lg.kimaru.expansion.sync

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TremapApi {

    @POST("/api/v2/users/login")
    @FormUrlEncoded
    fun loginUser(@Field("email") email: String, @Field("password") password: String): Call<ResponseBody>

    companion object {

        val apiVersion = "v2"
    }
}

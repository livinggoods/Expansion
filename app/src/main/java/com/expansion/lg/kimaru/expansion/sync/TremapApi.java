package com.expansion.lg.kimaru.expansion.sync;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TremapApi {

    @POST("/api/v1/users/login")
    @FormUrlEncoded
    Call<ResponseBody> loginUser(@Field("email") String email, @Field("password") String password);
}

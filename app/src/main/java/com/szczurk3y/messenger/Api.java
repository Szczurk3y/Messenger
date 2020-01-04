package com.szczurk3y.messenger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
    @Headers("Content-Type: application/json")
    @POST("register")
    Call<ResponseBody> register(@Body User user);
}

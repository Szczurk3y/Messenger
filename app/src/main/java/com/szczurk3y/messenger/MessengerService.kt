package com.szczurk3y.messenger

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MessengerService {
    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(@Body user: RegisterUser): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body user: LoginUser): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("invitations")
    fun getFriends(): Call<List<Friend>>
}
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

    @Headers("Content-Type: application/json", "auth-token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfdXNlcm5hbWUiOiJzaWVtYSIsImlhdCI6MTU3OTYxMzQzNX0.Y1YYwlVxM36dEY3Yy5D7waPeVp1Ge2FIqsiTgwZ5w9c")
    @POST("invitations")
    fun getInvitations(@Body invitation: Invitation): Call<List<Invitation>>
}
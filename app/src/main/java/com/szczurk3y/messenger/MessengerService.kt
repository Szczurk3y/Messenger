package com.szczurk3y.messenger

import Activities.UserContentActivity
import Fragments.LoginFragment
import android.graphics.Bitmap
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface MessengerService {
    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(@Body user: RegisterUser): Call<RegistrationServerResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body user: User): Call<LoginServerResponse>

    @Headers("Content-Type: application/json")
    @POST("invitations")
    fun getInvitations(@Header("auth-token") token: String, @Body invitation: Invitation): Call<MutableList<Invitation>>

    @Headers("Content-Type: application/json")
    @POST("invitations/sent")
    fun getSentInvitations(@Header("auth-token") token: String, @Body invitation: Invitation): Call<MutableList<Invitation>>

    @Headers("Content-Type: application/json")
    @POST("invitations/invite")
    fun inviteFriend(@Header("auth-token") token: String, @Body invitation: Invitation): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("invitations/refuse")
    fun refuseInvitation(@Header("auth-token") token: String, @Body invitation: Invitation): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("invitations/cancel")
    fun cancelInvitation(@Header("auth-token") token: String, @Body invitation: Invitation): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("friends")
    fun getFriends(@Header("auth-token") token: String, @Body friendsRelation: FriendsRelation): Call<MutableList<FriendsRelation>>

    @Headers("Content-Type: application/json")
    @POST("friends/add")
    fun addFriend(@Header("auth-token") token: String, @Body friendsRelation: FriendsRelation): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("friends/delete")
    fun deleteFriend(@Header("auth-token") token: String, @Body friendsRelation: FriendsRelation): Call<ResponseBody>


    @Headers("Content-Type: application/json")
    @PATCH("update")
    fun updateProfile(
        @Header("auth-token") token: String,
        @Body updatedUser: UpdatedUser
//        @Part(value = "userImage", encoding = "8-bit") image: Bitmap
    ): Call<UpdatedUser>

}
package com.szczurk3y.messenger

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface MessengerService {
    @Headers("Content-Type: application/json")
    @POST("register")
    fun register(@Body user: User): Call<RegistrationServerResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(
        @Body loginUser: LoginUser
    ): Call<LoginServerResponse>

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

    @Multipart
    @POST("profile/update")
    fun updateProfile(
        @Header ("auth-token") token: String,
        @Part image: MultipartBody.Part?,
        @Part username: MultipartBody.Part?,
        @Part password: MultipartBody.Part?,
        @Part email: MultipartBody.Part
    ): Call<UpdatedUserServerResponse>

    @Headers("Content-Type: application/json")
    @GET("profile/avatar")
    fun getAvatar(
        @Header("auth-token") token: String,
        @Query("username") username: String
    ): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("messages/chats")
    fun getChats(
        @Header("auth-token") token: String,
        @Query("username") username: String
    ): Call<MutableList<ChatItem>>
}
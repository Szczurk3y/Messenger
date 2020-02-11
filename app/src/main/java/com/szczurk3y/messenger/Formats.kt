package com.szczurk3y.messenger

import android.graphics.Bitmap
import retrofit2.http.Part

data class RegisterUser(
    val username: String,
    val email: String,
    val password: String
)

data class User(
    val username: String,
    val password: String
)

data class UpdatedUser(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val message: String = "",
    @Part(value = "userImage", encoding = "8-bit") val image: Bitmap
)

data class FriendsRelation(
    val username: String = "",
    val friend: String = ""
)

data class LoginServerResponse(
    val message: String = "",
    val isLogged: Boolean = false,
    val token: String = "",
    val email: String = ""
)

data class RegistrationServerResponse(
    val message: String = "",
    val isRegistered: Boolean = false
)

data class Invitation(
    val _id: String = "",
    val sendTime: String = "",
    val sender: String = "",
    val recipient: String = "",
    val __v: String = ""
)
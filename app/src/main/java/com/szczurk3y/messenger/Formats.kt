package com.szczurk3y.messenger

import android.view.View
import okhttp3.MultipartBody
import retrofit2.http.Part
import java.util.*

data class User(
    val username: String = "",
    val email: String = "",
    val password: String = ""
)

data class UpdatedUserServerResponse(
    val message: String = "",
    val isUpdated: Boolean = false
)

data class ChatItem(
    val friend: String = ""
)

data class Message(
    val nickname: String = "",
    val message: String = "",
    val date: String = ""
) {
    override fun toString(): String {
        return "{\"nickname\":\"${this.nickname}\", \"message\":\"${this.message}\", \"date\":\"${this.date}\"}"
    }
}

data class FriendsRelation(
    val username: String = "",
    val friend: String = "",
    val chat_room: String = ""
)

data class LoginUser(
    val username: String = "",
    val password: String = ""
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


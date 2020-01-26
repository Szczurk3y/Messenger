package com.szczurk3y.messenger

data class RegisterUser(
    val username: String,
    val email: String,
    val password: String
)

data class User(
    val username: String,
    val password: String
)

data class FriendsRelation(
    val username: String = "",
    val friend: String = ""
)

data class Invitation(
    val _id: String = "",
    val sendTime: String = "",
    val sender: String = "",
    val recipient: String,
    val __v: String = ""
)
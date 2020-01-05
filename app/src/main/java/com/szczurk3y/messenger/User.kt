package com.szczurk3y.messenger

data class RegisterUser(val username: String, val email: String, val password: String)
data class LoginUser(val username: String, val password: String)